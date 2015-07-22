package edu.nyu.acm.miparse

import java.io._
import java.lang.StringBuilder
import com.sun.jna.{ NativeLibrary, Platform, Pointer, WString }
import org.apache.tika.Tika
import org.apache.tika.mime.MediaType
import edu.nyu.acm.miparse.Protocol._

object Main extends App with MediaObjectSupport {

  val root = new File(args(0))
  val tika = new Tika()

  if(root.exists){ 
    
    val mediaObjects = iterate(root.listFiles)
    println(mediaObjects) 

  }

  def iterate(files: Array[File]): Vector[MediaObject] = {
    
    var mos = Vector.empty[MediaObject]

    files.foreach{ file =>
      if(file.isFile){

        val mediaType = MediaType.parse(tika.detect(file))

        val handle: Pointer = MediaInfoLibrary.INSTANCE.New()
        MediaInfoLibrary.INSTANCE.Open(handle, new WString(file.getAbsolutePath()))
        val mi = MediaInfoLibrary.INSTANCE.Inform(handle).toString()	
        val mo = new MOFactory().get(mi, file.getName, mediaType)
        mos = mos :+ mo
        
      } else if(file.isDirectory) {
        iterate(file.listFiles())
      }
    }

    mos

  }

}
