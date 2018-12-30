
package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.jblas.DoubleMatrix;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import main.KMeansCluster2;

public class KMeansTest2 {

	@Rule
	public TestName name = new TestName();

	@Test
	public void test1() {
		String testName = name.getMethodName();

		KMeansCluster2.main(("test\\" + testName + "\\date.txt").split("#"));
	}

	/**
	 * 50 numere
	 * valori: ( 0 - 10, 0 - 10)
	 */
	@Test
	public void test2() {
		String testName = name.getMethodName();

		KMeansCluster2.main(("test\\" + testName + "\\date.txt").split("#"));
	}

	/**
	 * 5.000 numere
	 * valori: ( 0 - 100, 0 - 100)
	 */
	@Test
	public void test3() {
		String testName = name.getMethodName();

		KMeansCluster2.main(("test\\" + testName + "\\date.txt").split("#"));
	}

	/**
	 * 10.000 numere
	 * valori: ( 0 - 1000, 0 - 1000)
	 */
	@Test
	public void test4() {
		String testName = name.getMethodName();

		KMeansCluster2.main(("test\\" + testName + "\\date.txt").split("#"));
	}

	/**
	 * 100.000 numere
	 * valori: ( 0 - 1000, 0 - 1000)
	 */
	@Ignore// Durata 59,849 s
	@Test
	public void test5() {
		String testName = name.getMethodName();

		KMeansCluster2.main(("test\\" + testName + "\\date.txt").split("#"));
	}

	/**
	 * 1.000.000 numere
	 * valori: ( 0 - 1000, 0 - 1000)
	 */
	@Ignore// Durata necunoscuta
	@Test
	public void test6() {
		String testName = name.getMethodName();

		KMeansCluster2.main(("test\\" + testName + "\\date.txt").split("#"));
	}

	@SuppressWarnings("unused")
	private void genereazaNumere() {
		File f = new File("out.txt");
		try (FileOutputStream out = new FileOutputStream(f)) {
			String string = "ha";
			DoubleMatrix rand = DoubleMatrix.rand(1000000000, 2);
			DoubleMatrix mul = rand.mul(1000);
			int[][] data = mul.toIntArray2();

			StringBuilder s = new StringBuilder();

			Arrays.stream(data).map(x -> x).forEach(columnArray -> {
				Arrays.stream(columnArray).forEach(element -> s.append(element + " "));
				s.append("\r\n");
			});

			string = s.toString();

			out.write(string.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
