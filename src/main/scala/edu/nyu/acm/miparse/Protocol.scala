package edu.nyu.acm.miparse

import org.apache.tika.mime.MediaType
import java.util.UUID

object Protocol {
  
  case class GeneralStream(uid: UUID, path: String, format : Option[String], formatProfile : Option[String], codecID: Option[String], duration: Option[String], library: Option[String] )
  case class AudioStream(uid: UUID, id: Option[String], format : Option[String], formatName: Option[String], formatProfile : Option[String], codecId: Option[String])
  case class VideoStream(uid: UUID, id: Option[String], format : Option[String], formatName: Option[String], formatProfile : Option[String], codecId: Option[String], standard: Option[String], colorSpace: Option[String], chroma: Option[String], bitDepth: Option[String])
  case class MediaObject(uid: UUID, filename: String, mediaType: MediaType, general: GeneralStream, audio: Vector[AudioStream], video: Vector[VideoStream])
  
}