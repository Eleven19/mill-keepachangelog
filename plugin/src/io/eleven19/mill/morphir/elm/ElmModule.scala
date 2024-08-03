package io.eleven19.mill.morphir.elm
import io.eleven19.mill.morphir.js.JsModule
import mill._
import mill.scalalib._

trait ElmModule extends JsModule {

    /**
     * All individual source files fed into the Elm or Morphir elm compiler.
     */
    override def allSourceFiles: T[Seq[PathRef]] = T {
        Lib.findSourceFiles(allSources(), Seq("elm")).map(PathRef(_))
    }
}
