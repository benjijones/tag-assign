package database

import java.io.File
import scala.collection.JavaConversions.asScalaIterator
import scala.collection.JavaConversions.asJavaMap
import scala.collection.JavaConverters._
import org.apache.avro.Schema.Parser
import org.apache.avro.generic.GenericData
import oracle.kv.Direction
import oracle.kv.Key
import oracle.kv.KeyValueVersion

/**
 * this dao should at some point be handled by a dependency injector
 * make it a class, have it be handled by an IOC manager, and
 * get new schema whenever needed
 */
object TagClassDao {
	
	private val tagClassSchema = new Parser parse new File("database/schema/TagClass.avsc")
	
	def getOrCreateByName(name : String) = {
		val store = DatabaseServer connect
		val binding = store.getAvroCatalog getGenericBinding tagClassSchema
		
		
		val rawTagClass = store.get(Key.createKey("TagClass", name))
		val result = rawTagClass match {
				case _ : Any => {
						val member = binding toObject rawTagClass.getValue
						val name = member.get("name").asInstanceOf[String]
						val documentCount = member.get("document_count").asInstanceOf[Int]
						val wordCounts = member.get("word_counts").asInstanceOf[java.util.Map[String,Int]].asScala.toMap
						TagClassDC(name, documentCount, wordCounts)
					}
				case null => {
						TagClassDC(name, 0, Map[String,Int]())
					}
			}
		
		store.close
		
		result
	}
	
	def getByNames(names : List[String]) = {
		getAll filter { names contains }
	}
	
	def getAll = {
		val store = DatabaseServer connect
		val binding = store.getAvroCatalog getGenericBinding tagClassSchema
		
		val rawTagClasses = store.multiGetIterator(Direction.FORWARD, 0, Key.createKey("TagClass"), null, null)
		val result = rawTagClasses.map { vv =>
			val member = binding toObject vv.getValue
			val name = member.get("name").asInstanceOf[String]
			val documentCount = member.get("document_count").asInstanceOf[Int]
			val wordCounts = member.get("word_counts").asInstanceOf[java.util.Map[String,Int]].asScala.toMap
			TagClassDC(name, documentCount, wordCounts)
		} toList
		
		store.close
		
		result
	}
	
	def getTotalDocumentCount = {
		val store = DatabaseServer connect
		val binding = store.getAvroCatalog getGenericBinding tagClassSchema
		
		val rawTagClasses = store.multiGetIterator(Direction.FORWARD, 0, Key.createKey("TagClass"), null, null)
		val result = rawTagClasses.map { vv =>
			val member = binding toObject vv.getValue
			member.get("document_count").asInstanceOf[Int]
		} reduce { _ + _ }
		
		store.close
		
		result
	}
	
	def addToTagClass(tagClass : TagClassDC) {
		val store = DatabaseServer connect
		val binding = store.getAvroCatalog getGenericBinding tagClassSchema
		
		val oldTagClass = getOrCreateByName(tagClass.name)
		
		println(oldTagClass.name + " " + oldTagClass.documentCount)
		
		val newTagClass = tagClass + oldTagClass
		
		val entity = new GenericData.Record(tagClassSchema)
		entity.put("name", newTagClass.name)
		entity.put("document_count", newTagClass.documentCount)
		entity.put("word_counts", asJavaMap(newTagClass.wordCounts))
		
		store.put(Key.createKey("TagClass"), binding toValue entity)
		
		store.close
	}
}