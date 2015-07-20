package edu.nyu.acm.miparse

import java.io.File
import java.util.UUID
import java.lang.StringBuilder

trait FormattingSupport extends IndexSupport {

  case class GeneralStream(uid: UUID, path: String, format : String, formatProfile : String, codecID: String, duration: String, library: String )
  case class AudioStream(uid: UUID, id: Int, format : String, formatName: Option[String], formatProfile : Option[String], codecId: String)
  case class videoStream(uid: UUID, id: Int, format : String, formatName: String, formatProfile : String, codecId: String, standard: Option[String], colorSpace: String, chroma: String, bitDepth: String)

   
  class Formatter {
    def format(textIn: String): Unit = {
      val lines = textIn.split("\\r?\\n")
      iterateIndexes(lines, getStreamIndexes(lines))   

    }

    def iterateIndexes(lines: Array[String], indexes: Vector[Tuple3[String, Int, Int]]): Unit = { 
      indexes.foreach { index =>
      
        val fields = getStream(lines, index)
                
        index._1 match {
            
            case("General") => {
              val g = new GeneralStream(UUID.randomUUID(), fields("Complete name"), fields("Format"), fields("Format profile"), fields("Codec ID"), fields("Duration"), fields("Writing library"))
              println("  " + g)  
            }

            case("Audio") => {
              val a = new AudioStream(
                UUID.randomUUID(), 
                fields("ID").toInt, 
                fields("Format"), 
                if(fields.isDefinedAt("Format/Info")) Some(fields("Format/Info")) else None, 
                if(fields.isDefinedAt("Format profile")) Some(fields("Format profile")) else None, 
                fields("Codec ID"))
              println("  " + a)
            }

            case("Video") => {
                val v = new videoStream(
                    UUID.randomUUID(), 
                    fields("ID").toInt, 
                    fields("Format"), 
                    fields("Format/Info"), 
                    fields("Format profile"), 
                    fields("Codec ID"), 
                    if(fields.isDefinedAt("Standard")) Some(fields("Standard")) else None, 
                    fields("Color space"), 
                    fields("Chroma subsampling"), 
                    fields("Bit depth"))

                println("  " + v)
            }

            case _ => 
        }
        
      }
    }
  }
}
