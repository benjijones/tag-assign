package main.scala.database

import java.io.File
import scala.collection.JavaConversions.asScalaIterator
import scala.collection.JavaConversions.asJavaMap
import scala.collection.JavaConverters._
import com.redis.RedisClient

/**
 * this dao should at some point be handled by a dependency injector
 * make it a class, have it be created by an IOC manager
 */
object TagClassDao {
	
	def getOrCreateByName(id : String) = {
		val store = DatabaseServer connect
		
		val opt = store.hget(List("tagclass", "names") mkString ":", id)
		val result = opt match {
			case Some(name) => {
				val documentCount = store.hget(List("tagclass", "document_counts") mkString ":", id).get.toInt
				val wordCounts = store.hgetall(List("tagclass", "word_counts", id) mkString ":").get.map{ case (word, count) => word -> count.toInt }
				TagClassDC(name, documentCount, wordCounts)
			}
			case None =>
				TagClassDC(id, 0, Map[String,Int]())
		}
		
		store.disconnect
		
		result
	}
	
	def getAll = {
		val store = DatabaseServer connect
		
		val names = store.hvals(List("tagclass", "names") mkString ":").get
		val result = {
			names map { 
				name => { 
					val documentCount = store.hget(List("tagclass", "document_counts") mkString ":", name).get.toInt
					val wordCounts = store.hgetall(List("tagclass", "word_counts", name) mkString ":").get.map{ case (word, count) => word -> count.toInt }
					TagClassDC(name, documentCount, wordCounts)
					}
				}
		}
		
		store.disconnect
		
		result
	}
	
	def addToTagClass(tagClass : TagClassDC) {
		val store = DatabaseServer connect
		
		val oldTagClass = getOrCreateByName(tagClass.name)
		
		val newTagClass = tagClass + oldTagClass
		
		store.hset(List("tagclass", "names") mkString ":", tagClass.name, tagClass.name)
		store.hset(List("tagclass", "document_counts") mkString ":", tagClass.name, newTagClass.documentCount)
		store.hmset(List("tagclass", "word_counts", tagClass.name) mkString ":", newTagClass.wordCounts)
		
		store.disconnect
	}
}