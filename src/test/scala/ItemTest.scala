import org.scalatest._
import flatspec._
import matchers._

class ItemTest extends AnyFlatSpec with should.Matchers {

  "An Item" should "sum num" in {
    val t = Item(2, "test")

    t.action1(1) should be(3)
    t.action1(-1) should be(1)
  }

  it should "concat str" in {
    val t = Item(2, "test")

    t.action2("ff") should be("fftest")
    t.action2("gg") should be("ggtest")
  }

}
