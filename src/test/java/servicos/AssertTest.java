package servicos;

import org.junit.Assert;
import org.junit.Test;

import entidades.Usuario;

public class AssertTest {

	@Test
	public void test() {
		Assert.assertTrue(true);
		Assert.assertFalse(false);

		Assert.assertEquals(1, 1);
		Assert.assertEquals(0.5123, 0.512, 0.001);
		Assert.assertEquals(Math.PI, 3.14, 0.01);
		
		int i1 =5;
		Integer i2 = 5;
		Assert.assertEquals(Integer.valueOf(i1), i2);
		Assert.assertEquals(i1, i2.intValue());
		
		Assert.assertEquals("bola","bola");
		Assert.assertNotEquals("bola","Bola");
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		
		Usuario u1 = new Usuario("user");
		Usuario u2 = new Usuario("user");
		Usuario u3 = u2;
		Usuario u4 = null;
		
		Assert.assertTrue(u1.equals(u2));
		Assert.assertSame(u2, u3);
		Assert.assertNotSame(u2, u4);
		Assert.assertNull(u4);
		Assert.assertNotNull(u1);
	}
}
