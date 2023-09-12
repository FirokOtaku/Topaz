package firok.topaz;

import firok.topaz.general.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
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
			var str = String.valueOf(step);
			if(step < 5) list5.add(str);
			if(step < 20) list20.add(str);
			list21.add(str);
		}
	}

	@Test
	public void trimGroup_20_2()
	{
		var groups = Collections.trimGroup(list20, 2);
		Assertions.assertEquals(10, groups.size());
		for(var group : groups)
		{
			Assertions.assertEquals(2, group.size());
		}
	}

	@Test
	public void trimGroup_21_2()
	{
		var groups = Collections.trimGroup(list21, 2);
		Assertions.assertEquals(11, groups.size());
		for(var step = 0; step < 11; step++)
		{
			Assertions.assertEquals(step == 10 ? 1 : 2, groups.get(step).size());
		}
	}

	@Test
	public void trimGroupReturnType()
	{
		var set = new HashSet<Integer>();
		var list = new ArrayList<Integer>();
		for(var step = 0; step < 15; step++)
		{
			set.add(step);
			list.add(step);
		}
		var groupsSet = Collections.trimGroup(set, 3);
		var groupsList = Collections.trimGroup(list, 3);
		Assertions.assertEquals(5, groupsSet.size());
		Assertions.assertEquals(5, groupsList.size());
		Assertions.assertInstanceOf(List.class, groupsSet);
		Assertions.assertInstanceOf(List.class, groupsList);
		for(var group : groupsSet)
		{
			Assertions.assertInstanceOf(Set.class, group);
		}
		for(var group : groupsList)
		{
			Assertions.assertInstanceOf(List.class, list);
		}
	}

	@Test
	public void trimGroup_20_6()
	{
		var groups = Collections.trimGroup(list20, 6);
		Assertions.assertEquals(4, groups.size());
		for(var step = 0; step < 4; step++)
		{
			Assertions.assertEquals(step == 3 ? 2 : 6, groups.get(step).size());
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

	@Test
	public void testSqueeze()
	{
		var list1 = new ArrayList<Integer>();
		list1.add(1);
		list1.add(null);
		list1.add(2);
		list1.add(null);
		list1.add(3);
		var list1s = Collections.squeeze(list1);
		Assertions.assertEquals(3, list1s.size());
		Assertions.assertEquals(1, list1s.get(0));
		Assertions.assertEquals(2, list1s.get(1));
		Assertions.assertEquals(3, list1s.get(2));

		var arr1 = new Integer[] { 1, null, 2, null, 3 };
		var arr1s = Collections.squeeze(arr1);
		Assertions.assertEquals(3, arr1s.length);
		Assertions.assertEquals(1, arr1s[0]);
		Assertions.assertEquals(2, arr1s[1]);
		Assertions.assertEquals(3, arr1s[2]);

		var arr2 = new Integer[] { 4, 5, 6 };
		var arr2s = Collections.squeeze(arr2);
		Assertions.assertEquals(3, arr2s.length);
		Assertions.assertEquals(4, arr2s[0]);
		Assertions.assertEquals(5, arr2s[1]);
		Assertions.assertEquals(6, arr2s[2]);

		var arr3 = new Integer[] { 7, 8, 9, null, null, null };
		var arr3s = Collections.squeeze(arr3);
		Assertions.assertEquals(3, arr3s.length);
		Assertions.assertEquals(7, arr3s[0]);
		Assertions.assertEquals(8, arr3s[1]);
		Assertions.assertEquals(9, arr3s[2]);

		var arr4 = new Integer[] { null, null, null, 10, 11, 12 };
		var arr4s = Collections.squeeze(arr4);
		Assertions.assertEquals(3, arr4s.length);
		Assertions.assertEquals(10, arr4s[0]);
		Assertions.assertEquals(11, arr4s[1]);
		Assertions.assertEquals(12, arr4s[2]);

		var arr5 = new Integer[] { null, null, null, null, null, null };
		var arr5s = Collections.squeeze(arr5);
		Assertions.assertEquals(0, arr5s.length);

		var arr6 = new Integer[] { 13, 14, 15, 16, 17, 18 };
		var arr6s = Collections.squeeze(arr6);
		Assertions.assertEquals(6, arr6s.length);
		Assertions.assertEquals(13, arr6s[0]);
		Assertions.assertEquals(18, arr6s[5]);
	}
}
