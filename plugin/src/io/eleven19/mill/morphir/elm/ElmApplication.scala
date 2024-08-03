package io.eleven19.mill.morphir.elm

import mill.{PathRef, T, Target, util}

trait ElmApplication extends ElmModule {
    def targetFileName = T("elm.js")

    def elmMake: Target[PathRef] = T {
        val moduleName = millSourcePath.last
        val destPath   = T.dest / targetFileName()

        val commandArgs = Seq(
            jsPackageManagerRunner(),
            "elm",
            "make",
            "--output",
            destPath.toString(),
        ) ++ allSourceFiles().map(_.path.toString)
        util.Jvm.runSubprocess(commandArgs, T.ctx().env, T.ctx().workspace)
        PathRef(destPath)
    }
}
