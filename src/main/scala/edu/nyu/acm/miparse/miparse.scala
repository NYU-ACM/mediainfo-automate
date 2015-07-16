package edu.nyu.acm.miparse

import java.io._
import com.sun.jna.NativeLibrary
import com.sun.jna.Platform
import com.sun.jna.Pointer
import com.sun.jna.WString

object Main extends App {

  val root = new File(args(0))
  val writer = new FileWriter(new File(args(1)))
  writer.write("report for: " + root.getAbsolutePath() + "\n\n")
  if(root.exists){ iterate(root.listFiles) }
  writer.close

  	 

  def iterate(files: Array[File]): Unit = {
    files.foreach{ file =>
      if(file.isFile){
        parse(file)	
      } else if(file.isDirectory) {
        iterate(file.listFiles())
      }
    }
  }

  def parse(file: File): Option[String] = {
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
}