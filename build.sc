import mill._
import mill.scalalib._
import mill.scalalib.scalafmt._
import mill.scalalib.publish._
import mill.scalalib.api.ZincWorkerUtil._
import os.Path

import $ivy.`com.carlosedp::mill-aliases::0.4.1`
import com.carlosedp.aliases._
import $ivy.`de.tototec::de.tobiasroeser.mill.integrationtest::0.7.1`
import de.tobiasroeser.mill.integrationtest._
import $ivy.`com.goyeau::mill-scalafix::0.3.1`
import com.goyeau.mill.scalafix.ScalafixModule
import $ivy.`io.chris-kipp::mill-ci-release::0.1.9`
import io.kipp.mill.ci.release._
import de.tobiasroeser.mill.vcs.version.VcsVersion

val millVersions = Seq("0.11.6")
val scala213     = "2.13.12"
val pluginName   = "mill-morphir"

object plugin extends Cross[Plugin](millVersions)
trait Plugin  extends Cross.Module[String]
    with ScalaModule
    with Publish
    with ScalafmtModule
    with ScalafixModule {
    val millVersion  = crossValue
    def scalaVersion = scala213
    def artifactName = s"${pluginName}_mill${scalaNativeBinaryVersion(millVersion)}"

    def compileIvyDeps = super.compileIvyDeps() ++ Agg(
        ivy"com.lihaoyi::mill-scalalib:${millVersion}",
        ivy"com.lihaoyi::mill-scalanativelib:${millVersion}",
        ivy"com.lihaoyi::mill-scalajslib:${millVersion}",
        ivy"org.scala-lang:scala-reflect:${this.scalaVersion()}",
    )

    def sources = T.sources {
        super.sources() ++ Seq(
            millSourcePath / s"src-mill${scalaNativeBinaryVersion(millVersion)}"
        ).map(PathRef(_))
    }

    object test extends ScalaTests with TestModule.Munit {
        override def ivyDeps: T[Agg[Dep]] = super.ivyDeps() ++ Agg(
            ivy"com.lihaoyi::mill-scalalib:${millVersion}",
            ivy"com.lihaoyi::mill-scalanativelib:${millVersion}",
            ivy"com.lihaoyi::mill-scalajslib:${millVersion}",
            ivy"org.scala-lang:scala-reflect:${this.scalaVersion()}",
            ivy"org.scalameta::munit::1.0.0-M10",
        )
    }
}

trait Publish extends CiReleaseModule {
    def pomSettings = PomSettings(
        description = "A mill plugin for working with Morphir projects and workspaces.",
        organization = "io.eleven19.mill",
        url = "https://github.com/eleven19/mill-morphir",
        licenses = Seq(License.`Apache-2.0`),
        versionControl = VersionControl.github("eleven19", "mill-morphir"),
        developers = Seq(
            Developer(
                "DamianReeves",
                "Damian Reeves",
                "https://github.com/DamianReeves",
            )
        ),
    )
    def publishVersion = VcsVersion.vcsState().format()
    def sonatypeHost   = Some(SonatypeHost.s01)
}

object itest     extends Cross[ItestCross](millVersions)
trait ItestCross extends MillIntegrationTestModule with Cross.Module[String] { self =>
    // override def millSourcePath: Path = super.millSourcePath / os.up
    def millVersion = crossValue
    override def millTestVersion: T[String] = self.millVersion
    def pluginsUnderTest = Seq(
        plugin(self.millVersion)
    )
    def testBase = millSourcePath / "src"
    def testInvocations = T {
        Seq(
            PathRef(testBase / "basic-standalone") -> Seq(
                TestInvocation.Targets(Seq("prepare")),
                TestInvocation.Targets(Seq("verify")),
            )
        )
    }
}

object MyAliases extends Aliases {
    def fmt      = alias("mill.scalalib.scalafmt.ScalafmtModule/reformatAll __.sources")
    def checkfmt = alias("mill.scalalib.scalafmt.ScalafmtModule/checkFormatAll __.sources")
    def lint     = alias("mill.scalalib.scalafmt.ScalafmtModule/reformatAll __.sources", "__.fix")
    def deps     = alias("mill.scalalib.Dependency/showUpdates")
    def pub      = alias("io.kipp.mill.ci.release.ReleaseModule/publishAll")
    def publocal = alias("__.publishLocal")
    def testall  = alias("__.test")
}
