package edu.nyu.acm.miparse

import java.io._
import java.lang.StringBuilder
import com.sun.jna.{ NativeLibrary, Platform, Pointer, WString }
import org.apache.tika.Tika
import org.apache.tika.mime.MediaType

object Main extends App with FormattingSupport {

  val root = new File(args(0))
  val tika = new Tika()

  if(root.exists){ 
    
    iterate(root.listFiles) 

  }

  def iterate(files: Array[File]): Unit = {
  
    files.foreach{ file =>
      if(file.isFile){

        val mediaType = MediaType.parse(tika.detect(file))

        val handle: Pointer = MediaInfoLibrary.INSTANCE.New()
        MediaInfoLibrary.INSTANCE.Open(handle, new WString(file.getAbsolutePath()))
        val mi = MediaInfoLibrary.INSTANCE.Inform(handle).toString()	
        val formatter = new Formatter()
        formatter.format(mi, file.getName, mediaType)
        
      } else if(file.isDirectory) {
        iterate(file.listFiles())
      }
    }
  }

/*
  def write(file: File): Option[String] = {
    try {
      val handle: Pointer = MediaInfoLibrary.INSTANCE.New()
      MediaInfoLibrary.INSTANCE.Open(handle, new WString(file.getAbsolutePath()))
      val body = MediaInfoLibrary.INSTANCE.Inform(handle).toString()
      writer.append("----------------------------------------------------------" + "\n")
      writer.append(file.getName() + "\n")
      writer.append("----------------------------------------------------------" + "\n")
      writer.append(body)
      writer.flush
      MediaInfoLibrary.INSTANCE.Close(handle)
      MediaInfoLibrary.INSTANCE.Delete(handle)
      Some("SUCCESS " + file.getName)
    } catch {
      case e: Exception => None      
    }
  }
  */
}
