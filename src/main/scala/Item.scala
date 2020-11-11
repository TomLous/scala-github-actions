case class Item(id: Int, name: String) {
  def action1(num: Int): Int = id + num
  def action2(str: String): String = str + name

}
