package io.eleven19.mill.morphir.js

import mill._
import mill.scalalib._

trait JsModule extends Module {
    def artifactName:           Target[String] = T(millSourcePath.last)
    def jsPackageManagerRunner: Target[String] = T("npx")
    def jsPackageManagerCmd:    Target[String] = T("npm")

    /**
     * The folders containing all source files fed into the compiler
     */
    def allSources: T[Seq[PathRef]] = T(sources() ++ generatedSources())

    /**
     * Folders containing source files that are generated rather than
     * hand-written; these files can be generated in this target itself, or can
     * refer to files generated from other targets
     */
    def generatedSources: T[Seq[PathRef]] = T(Seq.empty[PathRef])
    def sources = T.sources(millSourcePath / "src")

    /**
     * All individual source files fed into the compiler/tooling.
     */
    def allSourceFiles: T[Seq[PathRef]] = T {
        Lib.findSourceFiles(allSources(), Seq("js", "ts")).map(PathRef(_))
    }

}
