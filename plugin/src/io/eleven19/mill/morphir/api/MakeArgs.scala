package io.eleven19.mill.morphir.api

import mill.api.JsonFormatters._

case class MakeArgs(
    projectDir:  os.Path,
    output:      os.Path,
    indentJson:  Boolean,
    typesOnly:   Boolean,
    fallbackCli: Option[Boolean],
  ) { self =>
    def useFallbackCli: Boolean = self.fallbackCli.getOrElse(false)

    def toCommandArgs: Seq[String] =
        Seq(
            "make",
            "--output",
            output.toString(),
            if (indentJson) "--indent-json" else "",
            if (typesOnly) "--types-only" else "",
            if (useFallbackCli) "--fallback-cli" else "",
        )

    def toCommandArgs(cli: String): Seq[String] = Seq(cli) ++ toCommandArgs
}

object MakeArgs {
    implicit val jsonFormatter: upickle.default.ReadWriter[MakeArgs] = upickle.default.macroRW
}
