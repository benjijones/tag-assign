package main.scala.database

import com.redis.RedisClient

object DatabaseServer {

	def main(args : Array[String]) {
		
		println(TagClassDao.getAll.map{a => List(a.name, a.documentCount, a.wordCounts toString) })
	}
	
	def connect = new RedisClient("localhost", 6379)

}