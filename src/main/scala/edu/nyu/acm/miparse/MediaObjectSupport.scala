package edu.nyu.acm.miparse

import java.io.File
import java.util.UUID
import java.lang.StringBuilder
import edu.nyu.acm.miparse.Protocol._
import org.apache.tika.mime.MediaType

trait MediaObjectSupport extends IndexSupport {

 
  class MOFactory {

    def get(textIn: String, filename: String, mediaType: MediaType): MediaObject = {
      val lines = textIn.split("\\r?\\n")
      iterateIndexes(lines, getStreamIndexes(lines), filename, mediaType)   
    }

    def iterateIndexes(lines: Array[String], indexes: Vector[Tuple3[String, Int, Int]], filename: String, mediaType: MediaType): MediaObject = { 
      
      var general = Vector.empty[GeneralStream]
      var audio = Vector.empty[AudioStream]
      var video = Vector.empty[VideoStream]

      
      indexes.foreach { index =>    
        val fields = getStream(lines, index)             
        index._1 match {
            
            case("General") => {
              val g = new GeneralStream(
                UUID.randomUUID(), 
                fields("Complete name"), 
                fieldsContains("Format"), 
                fieldsContains("Format profile"), 
                fieldsContains("Codec ID"), 
                fieldsContains("Duration"), 
                fieldsContains("Writing library"))
              general = general :+ g
 
            }

            case("Audio") => {
              val a = new AudioStream(
                UUID.randomUUID(), 
                fieldsContains("ID"), 
                fieldsContains("Format"), 
                fieldsContains("Format/Info"), 
                fieldsContains("Format profile"), 
                fieldsContains("Codec ID"))
              audio = audio :+ a
            }

            case("Video") => {
                val v = new VideoStream(
                    UUID.randomUUID(), 
                    fieldsContains("ID"), 
                    fieldsContains("Format"), 
                    fieldsContains("Format/Info"), 
                    fieldsContains("Format profile"), 
                    fieldsContains("Codec ID"), 
                    fieldsContains("Standard"), 
                    fieldsContains("Color space"), 
                    fieldsContains("Chroma subsampling"), 
                    fieldsContains("Bit depth"))
                video = video :+ v
            }

            case _ => 
        }

        def fieldsContains(field: String): Option[String] = { if(fields.isDefinedAt(field)) Some(fields(field)) else None }
        
      }

      new MediaObject(UUID.randomUUID, filename, mediaType, general(0), audio, video)
    }
  }
}
