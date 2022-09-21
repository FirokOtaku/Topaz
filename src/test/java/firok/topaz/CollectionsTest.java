package firok.topaz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
}
