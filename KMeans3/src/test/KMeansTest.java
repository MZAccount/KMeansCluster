
package test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import main.KMeansCluster;

public class KMeansTest {

	@Rule
	public TestName name = new TestName();

	@Test
	public void test1() {
		String testName = name.getMethodName();

		KMeansCluster.main(("test\\"+testName+"\\date.txt").split("#"));

	}
}
