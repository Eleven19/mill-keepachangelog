import $file.plugins
import $ivy.`org.scalameta::munit:0.7.29`, munit.Assertions._
import mill._
import mill.define.Command
import mill.scalalib._
import mill.scalajslib._
import mill.scalanativelib._
import io.eleven19.mill.morphir._
import io.eleven19.mill.morphir.api.MakeResult
import io.eleven19.mill.morphir.elm._

object model extends MorphirElmModule {}

def prepare() = T.command {
    os.proc("npm", "install").call()
    ()
}

def verify(): Command[Unit] = T.command {
    val result            = model.morphirMake()
    val morphirIrJsonPath = result.irFilePath.path
    assertEquals(os.exists(morphirIrJsonPath), true)
    ()
}
