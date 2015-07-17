package edu.nyu.acm.miparse

import java.io.File

trait IndexSupport {
  
  val General = "^General".r
  val Audio = "^Audio".r
  val Video = "^Video".r
  val End = "^$".r

  var nums = Vector.empty[Int]
  var streamNums = Vector.empty[String]
  
  def getStreamIndexes(lines: Array[String]): Vector[Tuple3[String, Int, Int]] = {

		var lineNumber = 0
  	lines.foreach { line => 
  		line match {
  	  	case General() => { 
  	    	streamNums = streamNums :+ "General"
  	    	nums = nums :+ (lineNumber + 1)
  	  	}

  	  	case Audio() => { 
  	    	streamNums = streamNums :+ "Audio"
  	    	nums = nums :+ (lineNumber + 1)
  	  	}

	  	  case Video() => { 
	  	    streamNums = streamNums :+ "Video"
	  	    nums = nums :+ (lineNumber + 1)
	  	  }

  	  	case End() => nums = nums :+ lineNumber - 1
  	  
  	  	case _ => 
  		}

    	lineNumber = lineNumber + 1
  	}

  	nums = nums :+ lineNumber

  	getTuples()
  } 

  def getTuples(): Vector[Tuple3[String, Int, Int]] = {
    var tuples = Vector.empty[Tuple3[String, Int, Int]]
	  var m = 0
	  for(i <- 0 to nums.size - 1){ 
	  	if(i % 2 == 0 ) {
	  	  tuples = tuples :+ new Tuple3(streamNums(m), nums(i), nums(i + 1))
		    m = m + 1
			}
	  }	
		tuples   
	}

  def getStream(lines: Array[String], index: Tuple3[String, Int, Int]): Map[String, String] = {
    var fields = Map.empty[String, String]
    var lineNum = 0
    lines.foreach { line => 
      if(lineNum >= index._2 && lineNum <= index._3) {
        val x = line.split(" : ")
        fields = fields + (x(0).trim -> x(1).trim)
      }
      lineNum = lineNum + 1
    }
    fields
  }
}
