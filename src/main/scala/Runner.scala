object Runner extends App {
  val t1 = Item(1, "test1")
  val t2 = Item(2, "test2")

  val t3 = Item(t1.action1(t2.id), t2.action2(t2.name))

  println(t3.name)
  println(t3.id)

}
