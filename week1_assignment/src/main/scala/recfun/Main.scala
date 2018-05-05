package recfun

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
    def pascal(c: Int, r: Int): Int = {
      // pascal(c, r) = 0 if c > r
      // pascal(c, r) = 1 if c == 0
      // pascal(c, r) = pascal(c, r - 1) + pascal(c - 1, r - 1)
      if (c == 0) 1
      else {
        if (c > r) 0
        else pascal(c, r - 1) + pascal(c - 1, r - 1)
      }
    }
  
  /**
   * Exercise 2
   */
    def balance(chars: List[Char]): Boolean = {
      def loop(stack_count: Int, left: List[Char]): Boolean = {
        if (left.isEmpty){
          stack_count == 0
        }
        else{
          if (left.head == '(') {
            loop(stack_count + 1, left.tail)
          }
          else if (left.head == ')') {
            if (stack_count > 0) loop(stack_count - 1, left.tail) else false
          }
          else loop(stack_count, left.tail)
        }
      }
      loop(0, chars)
    }
  
  /**
   * Exercise 3
   */
    def countChange(money: Int, coins: List[Int]): Int = {
      if (coins.isEmpty) 0
      else if (0 == money) 1
      else if (money < 0) 0
      else {
        val a = countChange(money - coins.head, coins)
        val b = countChange(money, coins.tail)
        a + b
      }
    }
  }
