package microon.services.contacts.api.scala

import java.util.concurrent.Future

trait ContactsService {

  def listContacts: Future[Seq[Contact]]

}
