package example
import org.mongodb.scala._
import scala.io.Source 
import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.io._;
import java.io.File
import scala.io.StdIn.readLine;
import example.Helpers2._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala._
import org.mongodb.scala.model._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Sorts._

object MTGDB {
  def main(args: Array[String]) {
    println("Starting MongoDB - Scala Demo...")

    val client: MongoClient = MongoClient()
    val database: MongoDatabase = client.getDatabase("MagicDB")
    var Check: Boolean = true 
    var UserInput: String = ""
    var UserInput2: String = ""
    var UserInput3: String = ""
    var UserInput4: String = ""
    var UserInputName: String = ""
    var UserInputSet: String =""
    var UserInputQt: Int = 0;
    var UserInputBoard: String = "Main"
    var UserInputUpdateSTR: String = ""
    var UserInputUpdateINT: Int = 0;

    
    while (Check == true) {

      // Beginning of Application Menu

      println("Hi! Welcome to the Magic Card Collection Database. What would you like to do for today?:" +
        "\n A) Insert Card Information into the Database" +
        "\n B) Search Card Information from the Database"+
        "\n C) Update Card Information in the Database" +
        "\n D) Remove Card Information from the Database")

      UserInput= scala.io.StdIn.readLine()


      //First Functions: CREATION options
      if (UserInput.equalsIgnoreCase("A")) {

           println("What kind of Information would you like to insert today?:" +
             "\n A) Insert a Card Into the Card Catalogue" +
             "\n B) Insert Multiple Cards Into the Card Catalogue" +
             "\n C) Insert a Card Into a DeckList" +
             "\n D) Insert a DeckList into the Database")
           UserInput2= scala.io.StdIn.readLine()

           //Option 1-1: Single Card Insert into Card Catalogue(good)
           if (UserInput2.equalsIgnoreCase("A")){
              val collection: MongoCollection[Document] = database.getCollection("CardCatalogue")

              println("Please enter the Name of the Card you would like to enter:")
              UserInputName = scala.io.StdIn.readLine
              println("Please enter how many copies of this card you would like to enter:")
              UserInputQt = scala.io.StdIn.readInt()
              println("What Set is this card from?:")
              UserInputSet = scala.io.StdIn.readLine 
              val doc: Document = Document("Board" -> UserInputBoard,"Qty" -> UserInputQt, "Name" -> UserInputName, "Printing" -> UserInputSet, "Foil" -> "", "Alter" -> "", "Signed" -> "", "Condition" -> "", "Language" -> "")
              collection.insertOne(doc).printResults()

              collection.find().printResults("Here is the Collection after:") 
        }


          // 1-2: inserting a JSON file to the card catalogue(good)
           if (UserInput2.equalsIgnoreCase("B")){
              val collection: MongoCollection[Document] = database.getCollection("CardCatalogue")

              println("Please enter the file path of the JSON File for the cards you wish to insert here: ")
              UserInput3 = scala.io.StdIn.readLine
   
              val ImpFile = Source.fromFile(UserInput3).getLines.toList

              val StringToDB = ImpFile.map(doc => Document(doc))

              collection.insertMany(StringToDB).printResults()

              collection.find().printResults("Here is the Collection after:")  
           }

           //Option 1-3: Single Card Insert into a DeckList(good)
           if (UserInput2.equalsIgnoreCase("C")){
              println("Please Enter the Name of the Deck that you would like to Input:")
              UserInput3= scala.io.StdIn.readLine()

              val collection: MongoCollection[Document] = database.getCollection(UserInput3)

              println("Please enter the Name of the Card you would like to enter:")
              UserInputName = scala.io.StdIn.readLine
              println("Please enter how many copies of this card you would like to enter:")
              UserInputQt = scala.io.StdIn.readInt()
              println("What Set is this card from?:")
              UserInputSet = scala.io.StdIn.readLine()
              val doc: Document = Document("Board" -> UserInputBoard,"Qty" -> UserInputQt, "Name" -> UserInputName, "Printing" -> UserInputSet, "Foil" -> "", "Alter" -> "", "Signed" -> "", "Condition" -> "", "Language" -> "")
              collection.insertOne(doc).printResults()

              collection.find().printResults("Here is the Collection after:") 
        }

          //Option 1-4: DeckList Insert(good)
           if (UserInput2.equalsIgnoreCase("D")){
              println("Please Enter the Name of the Deck that you would like to Input:")
              UserInput3= scala.io.StdIn.readLine()

              val collection: MongoCollection[Document] = database.getCollection(UserInput3)

              println("Please enter the file path of the JSON File For the decklist here: ")
              UserInput3 = scala.io.StdIn.readLine
   
              val ImpFile = Source.fromFile(UserInput3).getLines.toList

              val StringToDB = ImpFile.map(doc => Document(doc))

              collection.insertMany(StringToDB).printResults()

              collection.find().printResults("Here is the Collection after:") 

        }
       

      }
      //Second Function Set: Read Functions (NOT DONE YET)
      if (UserInput.equalsIgnoreCase("B")) {
        println("What Would you like to search for?:" +
          "\n A) Individual Card" +
          "\n B) Group of Cards by Set" +
          "\n C) All Cards from a Decklist or from Card Catalogue")

        UserInput2= scala.io.StdIn.readLine()
        
          //2-1: Searching and printing Results of an individual card in the CardCatalogue
          if (UserInput2.equalsIgnoreCase("A")){
           
            println("Please enter either the name of the deck/collection you would like to look for a card in:")
            UserInput3= scala.io.StdIn.readLine()
            val collection: MongoCollection[Document] = database.getCollection(UserInput3)
            println("Please enter the Name of the Card That you would like to see:")
            UserInputName= scala.io.StdIn.readLine()
            collection.find(equal("Name", UserInputName)).printResults()
          }

           //2-2: Searching and printing Results of cards within a certain set
          if (UserInput2.equalsIgnoreCase("B")){
           
            println("Please enter the name of the deck/collection you would like to look for card from a certain set in:")
            UserInput3= scala.io.StdIn.readLine()
            val collection: MongoCollection[Document] = database.getCollection(UserInput3)
            println("Please enter the Set Acronym That you would like to look for:")
            UserInputSet= scala.io.StdIn.readLine()
            collection.find(equal("Printing", UserInputSet)).printResults()
          }
          
          //2-3: Printing out a Decklist
          if (UserInput2.equalsIgnoreCase("C")){
           println("Please enter either the name of the deck you would like to look at or 'Card Catalogue' to view non-deck cards available:")
           UserInput3= scala.io.StdIn.readLine()

           val collection: MongoCollection[Document] = database.getCollection(UserInput3)
           println("Here are the records we could find of this collection: ")
           collection.find().printResults()  
          }
      }

      //Third Functions: Update options
      if (UserInput.equalsIgnoreCase("C")) {

           println("What card Information would you like to update today?:" +
             "\n A) Update Individual Card" +
             "\n B) Update Multiple Cards In the Card Catalogue")
           UserInput2= scala.io.StdIn.readLine()
      
           //3-1: Updating a Specific Card
          if (UserInput2.equalsIgnoreCase("A")){
           
            println("Which Information would you like to Update?:" +
             "\n A) Name" +
             "\n B) QTY" +
             "\n C) Set" +
             "\n D) Foil" +
             "\n E) Signed" +
             "\n F) Alternate Art" +
             "\n G) Condition" +
             "\n H) Language")
            UserInput3= scala.io.StdIn.readLine()

            //Changing Card Name
            if (UserInput3.equalsIgnoreCase("A")){
              println("Which Collection would you like to look at?")
              UserInput4= scala.io.StdIn.readLine()
              val collection: MongoCollection[Document] = database.getCollection(UserInput4)
              println("Please enter the Name of the Card That you would like to see:")
              UserInputName= scala.io.StdIn.readLine()
              println("Please enter the new name for the card:")
              UserInputUpdateSTR= scala.io.StdIn.readLine()
              collection.updateOne(equal("Name", UserInputName),set("Name", UserInputUpdateSTR)).printHeadResult()
            }
            // Changing Card QTY
            if (UserInput3.equalsIgnoreCase("B")){
              println("Which Collection would you like to look at?")
              UserInput4= scala.io.StdIn.readLine()
              val collection: MongoCollection[Document] = database.getCollection(UserInput4)
              println("Please enter the Name of the Card That you would like to see:")
              UserInputName= scala.io.StdIn.readLine()
              println("Please enter the new quanitity of the card:")
              UserInputUpdateINT= scala.io.StdIn.readInt()
              collection.updateOne(equal("Name", UserInputName),set("Qty", UserInputUpdateINT)).printHeadResult()
            }
            //Changing Card SET
            if (UserInput3.equalsIgnoreCase("C")){
              println("Which Collection would you like to look at?")
              UserInput4= scala.io.StdIn.readLine()
              val collection: MongoCollection[Document] = database.getCollection(UserInput4)
              println("Please enter the Name of the Card That you would like to see:")
              UserInputName= scala.io.StdIn.readLine()
              println("Please enter the new Set for the card:")
              UserInputUpdateSTR= scala.io.StdIn.readLine()
              collection.updateOne(equal("Name", UserInputName),set("Printing", UserInputUpdateSTR)).printHeadResult()
            }
            //Changing Card Foil
            if (UserInput3.equalsIgnoreCase("D")){
              println("Which Collection would you like to look at?")
              UserInput4= scala.io.StdIn.readLine()
              val collection: MongoCollection[Document] = database.getCollection(UserInput4)
              println("Please enter the Name of the Card That you would like to see:")
              UserInputName= scala.io.StdIn.readLine()
              println("Please enter the foil status for the card:")
              UserInputUpdateSTR= scala.io.StdIn.readLine()
              collection.updateOne(equal("Name", UserInputName),set("Foil", UserInputUpdateSTR)).printHeadResult()
            }
            //Changing Card Signed Status
            if (UserInput3.equalsIgnoreCase("E")){
              println("Which Collection would you like to look at?")
              UserInput4= scala.io.StdIn.readLine()
              val collection: MongoCollection[Document] = database.getCollection(UserInput4)
              println("Please enter the Name of the Card That you would like to see:")
              UserInputName= scala.io.StdIn.readLine()
              println("Please enter the signature status for the card:")
              UserInputUpdateSTR= scala.io.StdIn.readLine()
              collection.updateOne(equal("Name", UserInputName),set("Signed", UserInputUpdateSTR)).printHeadResult()
            }
            //Changing Card Alternate Art Status
            if (UserInput3.equalsIgnoreCase("F")){
              println("Which Collection would you like to look at?")
              UserInput4= scala.io.StdIn.readLine()
              val collection: MongoCollection[Document] = database.getCollection(UserInput4)
              println("Please enter the Name of the Card That you would like to see:")
              UserInputName= scala.io.StdIn.readLine()
              println("Please enter the alternate art status for the card:")
              UserInputUpdateSTR= scala.io.StdIn.readLine()
              collection.updateOne(equal("Name", UserInputName),set("Alter", UserInputUpdateSTR)).printHeadResult()
            }
            //Changing Card COndition
              if (UserInput3.equalsIgnoreCase("G")){
             println("Which Collection would you like to look at?")
              UserInput4= scala.io.StdIn.readLine()
              val collection: MongoCollection[Document] = database.getCollection(UserInput4)
              println("Please enter the Name of the Card That you would like to see:")
              UserInputName= scala.io.StdIn.readLine()
              println("Please enter the condition status for the card:")
              UserInputUpdateSTR= scala.io.StdIn.readLine()
              collection.updateOne(equal("Name", UserInputName),set("Condition", UserInputUpdateSTR)).printHeadResult()
            }
            //Changing Card Language
            if (UserInput3.equalsIgnoreCase("H")){
              println("Which Collection would you like to look at?")
              UserInput4= scala.io.StdIn.readLine()
              val collection: MongoCollection[Document] = database.getCollection(UserInput4)
              println("Please enter the Name of the Card That you would like to see:")
              UserInputName= scala.io.StdIn.readLine()
              println("Please enter the language used on the card:")
              UserInputUpdateSTR= scala.io.StdIn.readLine()
              collection.updateOne(equal("Name", UserInputName),set("Language", UserInputUpdateSTR)).printHeadResult()
            }
          }

           //3-2: Updating a Group of Cards
          if (UserInput2.equalsIgnoreCase("B")){
           
            println("Information would you like to Update?:" +
             "\n A) QTY" +
             "\n B) Set")
             
            UserInput3= scala.io.StdIn.readLine()

            //Changing Card Name
            if (UserInput3.equalsIgnoreCase("A")){
              println("Which Collection would you like to look at?")
              UserInput4= scala.io.StdIn.readLine()
              val collection: MongoCollection[Document] = database.getCollection(UserInput4)
              println("Please enter the Starting Quantity:")
              UserInputQt= scala.io.StdIn.readInt()
              println("Please enter the Updated Quanity:")
              UserInputUpdateINT= scala.io.StdIn.readInt()
              collection.updateMany(equal("Qty", UserInputQt), set("Qty", UserInputUpdateINT)).printHeadResult()
            }
            // Changing Card QTY
            if (UserInput3.equalsIgnoreCase("B")){
              println("Which Collection would you like to look at?")
              UserInput4= scala.io.StdIn.readLine()
              val collection: MongoCollection[Document] = database.getCollection(UserInput4)
              println("Please enter the Starting Set Acronym:")
              UserInputSet= scala.io.StdIn.readLine()
              println("Please enter the Updated Set Acronym:")
              UserInputUpdateSTR= scala.io.StdIn.readLine()
              collection.updateMany(equal("Printing", UserInputSet), set("Printing", UserInputUpdateSTR)).printHeadResult()
            }
            
          }
        }
      //Fourth Function Set: Delete Functions
      if (UserInput.equalsIgnoreCase("D")) {
        println("What would you like to remove?" +
          "\n A) Individual Card" +
          "\n B) Cards From a Specific Set" +
          "\n C) All Cards from a Decklist or from Card Catalogue")

        UserInput2= scala.io.StdIn.readLine()

        //4-1: Delete a specific Card from a collection
        if (UserInput2.equalsIgnoreCase("A")){
           println("Which Card Collection would you like to remove a card from?:")
           UserInput3 = scala.io.StdIn.readLine()
           val collection: MongoCollection[Document] = database.getCollection(UserInput3)
           println("Please enter the Name of the Card That you would like to Remove:")
           UserInputName= scala.io.StdIn.readLine()
           collection.deleteOne(equal("Name", UserInputName)).printHeadResult("Record Removed: ") 
        }
        //4-2: Delete a set of cards from a Collection
         if (UserInput2.equalsIgnoreCase("B")){
          println("Which Card Collection would you like to edit?:")
          UserInput3 = scala.io.StdIn.readLine()
          val collection: MongoCollection[Document] = database.getCollection(UserInput3)
          println("Which Card Set would you like to remove from the list?:")
          UserInputSet = scala.io.StdIn.readLine()
          collection.deleteMany(equal("Printing", UserInputSet)).printHeadResult("Record Removed:") }



        //4-3: Delete Card Collection Records/ Collections
        if (UserInput2.equalsIgnoreCase("C")){
          println("Which Card Collection would you like to remove?:")
          UserInput3 = scala.io.StdIn.readLine()
          val collection: MongoCollection[Document] = database.getCollection(UserInput3)
          collection.deleteMany(gt("Qty", 0)).printResults()
          println("The card records from this catalogue have been removed. Would you like to remove the collection as well?:" +
            "\n A)Y " +
            "\n B)N")
          UserInput3= scala.io.StdIn.readLine()
          if (UserInput3.equalsIgnoreCase("A")) {
            collection.drop()
            println("Understood. The Collection has been removed.")
          }
           
        }
      }
          
      

      

      println("Would you like to continue running the program? (Y/N)")

      UserInput = scala.io.StdIn.readLine()

      if (UserInput.equalsIgnoreCase("N")) {
        Check = false
        println("Thank You. Have a nice day!")
      }

    }

    client.close()
  }
}
