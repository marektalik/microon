package microon.services.contacts.google

import com.google.gdata.client.contacts.{ContactsService => GoogleService}
import java.util.concurrent.{Executors, Future}
import microon.services.contacts.api.scala.{Contact, ContactsService}
import java.net.URL
import com.google.gdata.data.contacts.ContactFeed
import scala.collection.JavaConversions._
import microon.spi.scala.activeobject.ActiveObject
import microon.ri.activeobject.ExecutorServiceActiveObjectDispatcher

class GoogleContactsService(userEmail: String, userPassword: String) extends ContactsService with ActiveObject {

  val service = new GoogleService("Microon contact service.")
  service.setUserCredentials(userEmail, userPassword)

  activeObjectDispatcher(new ExecutorServiceActiveObjectDispatcher(Executors.newSingleThreadExecutor()))

  def listContacts: Future[Seq[Contact]] = dispatch {
    val feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full")
    val resultFeed = service.getFeed(feedUrl, classOf[ContactFeed])
    resultFeed.getEntries.map {
      contact => new Contact(contact.getEmailAddresses.map(_.getAddress))
    }
  }

}