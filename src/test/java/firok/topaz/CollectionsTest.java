package firok.topaz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class CollectionsTest
{
	static List<String> list5;
	static List<String> list20;
	static List<String> list21;
	static
	{
		list5 = new ArrayList<>();
		list20 = new ArrayList<>();
		list21 = new ArrayList<>();
		for(var step = 0; step < 21; step ++)
		{
			if(step < 5) list5.add("");
			if(step < 20) list20.add("");
			list21.add("");
		}
	}

//	@Test
	public void trimGroup_20_2()
	{
		var groups = Collections.trimGroup(list20, 2);
		Assertions.assertEquals(10, groups.size());
		for(var group : groups)
		{
			Assertions.assertEquals(2, group.size());
		}
	}

//	@Test
	public void trimGroup_21_2()
	{
		var groups = Collections.trimGroup(list21, 2);
		Assertions.assertEquals(11, groups.size());
		for(var step = 0; step < 11; step++)
		{
			Assertions.assertEquals(step == 10 ? 1 : 2, groups.size());
		}
	}

	@Test
	public void testCut()
	{
		var list1 = new ArrayList<String>();
		list1.add("1");
		list1.add("2");
		list1.add("3");
		list1.add("4");
		list1.add("5");
		var list1cut3 = Collections.cut(3, list1);
		Assertions.assertEquals(3, list1cut3.size());
		var list1cut4 = Collections.cut(4, list1);
		Assertions.assertEquals(4, list1cut4.size());
		System.out.println(list1cut3);
		System.out.println(list1cut4);

		var list2 = new ArrayList<String>();
		list2.add("1");
		list2.add("2");
		var list2cut3 = Collections.cut(3, list2);
		Assertions.assertEquals(2, list2cut3.size());
		System.out.println(list2cut3);
	}

	@Test
	public void testReverse()
	{
		var res1 = new int[] { 5, 4, 3, 2, 1, };
		Collections.makeReverse(res1);
		Assertions.assertArrayEquals(new int[] { 1, 2, 3, 4, 5, }, res1);

		var res2 = new int[] { 6, 5, 4, 3, 2, 1, };
		Collections.makeReverse(res2);
		Assertions.assertArrayEquals(new int[] { 1, 2, 3, 4, 5, 6, }, res2);

		var l1 = new ArrayList<Integer>();
		var l2 = new ArrayList<Integer>();
		IntStream.range(0, 100).forEach(num -> {
			l1.add(99 - num);
			l2.add(num);
		});
		var a1 = l1.toArray(new Integer[0]);
		var a2 = l2.toArray(new Integer[0]);
		Collections.makeReverse(a2);
		Assertions.assertArrayEquals(a1, a2);
	}
}
