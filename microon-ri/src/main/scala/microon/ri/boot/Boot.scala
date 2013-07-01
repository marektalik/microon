package microon.ri.boot

trait Boot {

  def start(): Boot

  def stop()

}
