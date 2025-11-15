scala> def genKey(length: Int): String = {
     | var lkey: String = "";
     | for(x <- scala.util.Random.alphanumeric take length)
     |  lkey+=x
     | 
     | return lkey;
     | }
def genKey(length: Int): String

scala> val jk1=genKey(8)
val jk1: String = 4h6Kb48Z

scala> val jk2=genKey(8)
val jk2: String = G8eskJyK

-- using a Map to store values
scala> val store = scala.collection.mutable.Map[String, String]()
val store: scala.collection.mutable.Map[String,String] = HashMap()

scala> store(genKey(10))="http://www.oracle.com"

scala> store(genKey(10))="https://alvinalexander.com/scala/how-to-add-update-remove-mutable-map-elements-scala-cookbook"

scala> store
val res18: scala.collection.mutable.Map[String,String] = HashMap(vvhsoHzlIb -> http://www.oracle.com, xls07SLowG -> https://alvinalexander.com/scala/how-to-add-update-remove-mutable-map-elements-scala-cookbook)

scala> println(store("vvhsoHzlIb")
     | )
http://www.oracle.com

scala> println(store("vvhsoHzlIb"))
http://www.oracle.com

scala> println(store("xls07SLowG"))
https://alvinalexander.com/scala/how-to-add-update-remove-mutable-map-elements-scala-cookbook



