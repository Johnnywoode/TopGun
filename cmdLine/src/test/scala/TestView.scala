import org.junit.{Assert, Before, Test}
import org.kohsuke.args4j.CmdLineParser
import topgun.cmdline._
import topgun.core.CallSite

import java.nio.file.Paths

class TestView {

  val param:Array[String] = Array("--jfr", getAbsolutePath, "--outDir", getTempDirectory, "--userCodePackagePrefix", "com.tradeengine.trade.engine")
  val options = new JfrParseCommandLine
  val parser = new CmdLineParser(options)

  @Before
  def setup(): Unit = {
    parser.parseArgument(param: _*)
    options.validate(parser)
  }

  //Getting the Absolute Path for the jfr file.
  private def getAbsolutePath = {
    val res = getClass.getClassLoader.getResource("jfr_files/scala.jfr")
    val file = Paths.get(res.toURI).toFile
    file.getAbsolutePath
  }

  private def getTempDirectory = System.getProperty("java.io.tmpdir")

  @Test def testAggregation(): Unit = {
    val totals = new Totals
    new FileParser(options.jfr, options, totals, new Configuration(options)).parse()

    val callSitesList: List[CallSite] = CallSite.allSites.toList

    val pcml = callSitesList.filter(_.view.equals(AggregateView.PACKAGE_CLASSNAME_METHOD_LINE_VIEW))
    val pcm = callSitesList.filter(_.view.equals(AggregateView.PACKAGE_CLASSNAME_METHOD_VIEW))
    val pc = callSitesList.filter(_.view.equals(AggregateView.PACKAGE_CLASSNAME_VIEW))
    val psl = callSitesList.filter(_.view.equals(AggregateView.PACKAGE_SOURCE_LINE_VIEW))
    val psm = callSitesList.filter(_.view.equals(AggregateView.PACKAGE_SOURCE_METHOD_VIEW))
    val pv = callSitesList.filter(_.view.equals(AggregateView.PACKAGE_VIEW))

    Assert.assertEquals(1120, pcml.length,0.0)
    Assert.assertEquals(707.0, pcm.length, 0.0)
    Assert.assertEquals(314.0, pc.length, 0.0)
    Assert.assertEquals(1066.0, psl.length, 0.0)
    Assert.assertEquals(654.0, psm.length, 0.0)
    Assert.assertEquals(50.0, pv.length, 0.0)


    Assert.assertEquals(39483.267, pcml.map(_.allDeratedAllocatedBytes.get).toList.max, 0.0)
    Assert.assertEquals(70446.972, pcm.map(_.allDeratedAllocatedBytes.get).toList.max, 0.0)
    Assert.assertEquals(162386.276, pc.map(_.allDeratedAllocatedBytes.get).toList.max, 0.0)
    Assert.assertEquals(45622.035, psl.map(_.allDeratedAllocatedBytes.get).toList.max, 0.0)
    Assert.assertEquals(70446.972, psm.map(_.allDeratedAllocatedBytes.get).toList.max, 0.0)
    Assert.assertEquals(241397.386, pv.map(_.allDeratedAllocatedBytes.get).toList.max, 0.0)


    Assert.assertEquals(7082.0, pcml.map(_.nativeAllDeratedCpu.get).toList.max, 0.0)
    Assert.assertEquals(8726.326, pcm.map(_.nativeAllDeratedCpu.get).toList.max, 0.0)
    Assert.assertEquals(22358.812, pc.map(_.nativeAllDeratedCpu.get).toList.max, 0.0)
    Assert.assertEquals(7082.0, psl.map(_.nativeAllDeratedCpu.get).toList.max,0.0)
    Assert.assertEquals(8726.326, psm.map(_.nativeAllDeratedCpu.get).toList.max, 0.0)
    Assert.assertEquals(33257.925, pv.map(_.nativeAllDeratedCpu.get).toList.max, 0.0)


    Assert.assertEquals(877.0, pcml.map(_.transitiveCpu.get).toList.max, 0.0)
    Assert.assertEquals(1269.0, pcm.map(_.transitiveCpu.get).toList.max, 0.0)
    Assert.assertEquals(3157.0, pc.map(_.transitiveCpu.get).toList.max, 0.0)
    Assert.assertEquals(1594.0, psl.map(_.transitiveCpu.get).toList.max, 0.0)
    Assert.assertEquals(2113.0, psm.map(_.transitiveCpu.get).toList.max, 0.0)
    Assert.assertEquals(4988.0, pv.map(_.transitiveCpu.get).toList.max, 0.0)


    Assert.assertEquals(7082.0, pcml.map(_.nativeAllFirstCpu.get).toList.max, 0.0)
    Assert.assertEquals(7082.0, pcm.map(_.nativeAllFirstCpu.get).toList.max, 0.0)
    Assert.assertEquals(7082.0, pc.map(_.nativeAllFirstCpu.get).toList.max, 0.0)
    Assert.assertEquals(7082.0, psl.map(_.nativeAllFirstCpu.get).toList.max, 0.0)
    Assert.assertEquals(7082.0, psm.map(_.nativeAllFirstCpu.get).toList.max, 0.0)
    Assert.assertEquals(10436.0, pv.map(_.nativeAllFirstCpu.get).toList.max, 0.0)


    Assert.assertEquals(0.0, pcml.map(_.nativeUserFirstCpu.get).toList.max, 0.0)
    Assert.assertEquals(0.0, pcm.map(_.nativeUserFirstCpu.get).toList.max, 0.0)
    Assert.assertEquals(0.0, pc.map(_.nativeUserFirstCpu.get).toList.max, 0.0)
    Assert.assertEquals(0.0, psl.map(_.nativeUserFirstCpu.get).toList.max, 0.0)
    Assert.assertEquals(0.0, psm.map(_.nativeUserFirstCpu.get).toList.max, 0.0)
    Assert.assertEquals(0.0, pv.map(_.nativeUserFirstCpu.get).toList.max, 0.0)

  }
}
