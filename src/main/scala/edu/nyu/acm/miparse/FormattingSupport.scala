package edu.nyu.acm.miparse

import java.io.File
import java.util.UUID
import java.lang.StringBuilder

trait FormattingSupport extends IndexSupport {

	case class GeneralStream(uid: UUID, path: String, format : String, formatProfile : String, codecID: String, duration: String, library: String )
	case class audioStream(uid: UUID, id: Int, format : String, formatName: String, formatProfile : String, codecId: String)
	case class videoStream(uid: UUID, id: Int, format : String, formatName: String, formatProfile : String, codecId: String, standard: String, colorSpace: String, chroma: String, bitDepth: String)

  def format(textIn: String): String = {
    val sb = new StringBuilder()

    val lines = textIn.split("\\r?\\n")
    val indexes = getStreamIndexes(lines)  
    
    indexes.foreach { index =>
    	val fields = getStream(lines, index)
    	index._1 match {
    		
    		case("General") => {
    			val g = new GeneralStream(UUID.randomUUID(), fields("Complete name"), fields("Format"), fields("Format profile"), fields("Codec ID"), fields("Duration"), fields("Writing library"))
  				sb.append(g)  
    		}

    		case("Audio") => {
    			val a = new audioStream(UUID.randomUUID(), fields("ID").toInt, fields("Format"), fields("Format/Info"), fields("Format profile"), fields("Codec ID"))
    			sb.append(a)
    		}

     		case("Video") => {
    			val v = new videoStream(UUID.randomUUID(), fields("ID").toInt, fields("Format"), fields("Format/Info"), fields("Format profile"), fields("Codec ID"), fields("Standard"), fields("Color space"), fields("Chroma subsampling"), fields("Bit depth"))
    			sb.append(v)
    		}

    		case _ =>
    	}
    }

    sb.toString()
  }

}