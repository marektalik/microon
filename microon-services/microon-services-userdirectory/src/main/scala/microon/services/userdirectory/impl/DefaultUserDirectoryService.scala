package microon.services.userdirectory.impl

import java.util.concurrent.Future
import com.mongodb.{DBCollection, BasicDBObject}
import org.springframework.data.mongodb.core.{MongoTemplate, CollectionCallback}
import scalapi.jdk.Implicits._
import microon.spi.scala.activeobject.ActiveObject
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.{Update, Query}
import scala.collection.JavaConversions._
import org.bson.types.ObjectId
import microon.services.userdirectory.{UserDirectoryService, User}

class DefaultUserDirectoryService(mongo: MongoTemplate) extends UserDirectoryService with ActiveObject {

  def userExists(id: String): Boolean =
    mongo.count(id.toObjectIdQuery, DefaultUserDirectoryService.userCollectionName) > 0

  def createUser(initialProperties: Map[String, String] = Map.empty): String = {
    mongo.execute(DefaultUserDirectoryService.userCollectionName, new CollectionCallback[String] {
      def doInCollection(collection: DBCollection): String = {
        val user = new BasicDBObject()
        initialProperties.map(kv => user.put(kv._1, kv._2))
        collection.insert(user)
        user.getString("_id")
      }
    })
  }

  def loadUserProperties(id: String): Map[String, String] =
    mongo.findOne(id.toObjectIdQuery, classOf[java.util.HashMap[String, String]], DefaultUserDirectoryService.userCollectionName).toMap - "_id"

  def loadUserProperty(id: String, property: String): Option[String] =
    mongo.findOne(id.toObjectIdQuery, classOf[java.util.HashMap[String, String]], DefaultUserDirectoryService.userCollectionName).toMap.
      get(property)

  def loadUserIdByProperty(propertyName: String, propertyValue: String): Future[Option[String]] = dispatch {
    mongo.findOne(new Query(where(propertyName).is(propertyValue)), classOf[java.util.HashMap[String, String]], DefaultUserDirectoryService.userCollectionName).toMap.get("_id")
  }

  def listUsersProperties(properties: Seq[String]): Seq[User] = {
    mongo.execute(DefaultUserDirectoryService.userCollectionName, new CollectionCallback[Seq[User]] {
      def doInCollection(collection: DBCollection): Seq[User] = {
        val users = for (user <- collection.find.iterator()) yield {
          val keys = user.keySet().toSet.intersect(properties.toSet)
          val props = keys.foldLeft(Map.empty[String, String])((props, key) => props + (key -> user.get(key).toString))
          User(user.get("_id").toString, props)
        }
        users.toList
      }
    })
  }

  def updateUserProperties(id: String, properties: Map[String, String]) {
    mongo.updateFirst(id.toObjectIdQuery, new Update().evaluate(up => properties.foreach(kv => up.set(kv._1, kv._2))), DefaultUserDirectoryService.userCollectionName)
  }

  // Helpers

  implicit class RichString(string: String) {
    def toObjectIdQuery: Query = new Query(where("_id").is(new ObjectId(string)))
  }

}

object DefaultUserDirectoryService {
  val userDirectoryDBName = "microon_services_userdirectory"
  val userCollectionName = "users"
}