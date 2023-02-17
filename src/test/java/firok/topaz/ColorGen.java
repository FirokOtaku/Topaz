package firok.topaz;

import firok.topaz.design.ChineseSolarTermColors;
import firok.topaz.design.CssColors;
import org.junit.jupiter.api.Test;

public class ColorGen
{
	@Test
	void genChinese() throws Exception
	{
		var fields = ChineseSolarTermColors.class.getFields();
		for(var field : fields)
		{
			var name = field.getName();
			var rgb = (int) field.get(null);
			var color = Integer.toHexString(rgb).toUpperCase();

			var jf = new StringBuilder();

			switch (name)
			{
				case "黄白游" -> jf.append("\n\n/* == 立春 == */\n\n");
				case "盈盈" -> jf.append("\n\n/* == 雨水 == */\n\n");
				case "赤缇" -> jf.append("\n\n/* == 惊蛰 == */\n\n");
				case "皦玉" -> jf.append("\n\n/* == 春分 == */\n\n");
				case "紫蒲" -> jf.append("\n\n/* == 清明 == */\n\n");
				case "昌荣" -> jf.append("\n\n/* == 谷雨 == */\n\n");
				case "青粲" -> jf.append("\n\n/* == 立夏 == */\n\n");
				case "彤管" -> jf.append("\n\n/* == 小满 == */\n\n");
				case "筠雾" -> jf.append("\n\n/* == 芒种 == */\n\n");
				case "赩炽" -> jf.append("\n\n/* == 夏至 == */\n\n");
				case "骍刚" -> jf.append("\n\n/* == 小暑 == */\n\n");
				case "夕岚" -> jf.append("\n\n/* == 大暑 == */\n\n");
				case "窃蓝" -> jf.append("\n\n/* == 立秋 == */\n\n");
				case "退红" -> jf.append("\n\n/* == 处暑 == */\n\n");
				case "凝脂" -> jf.append("\n\n/* == 白露 == */\n\n");
				case "卵色" -> jf.append("\n\n/* == 秋分 == */\n\n");
				case "醽醁" -> jf.append("\n\n/* == 寒露 == */\n\n");
				case "银朱" -> jf.append("\n\n/* == 霜降 == */\n\n");
				case "半见" -> jf.append("\n\n/* == 立冬 == */\n\n");
				case "龙膏烛" -> jf.append("\n\n/* == 小雪 == */\n\n");
				case "粉米" -> jf.append("\n\n/* == 大雪 == */\n\n");
				case "银红" -> jf.append("\n\n/* == 冬至 == */\n\n");
				case "酂白" -> jf.append("\n\n/* == 小寒 == */\n\n");
				case "紫府" -> jf.append("\n\n/* == 大寒 == */\n\n");
			}

			jf.append("/**\n")
					.append(" * <span style=\"color: #").append(color).append("; font-family: 隶书; font-size: 48px\">").append(name).append("</span>\n")
					.append(" * <div style=\"width: 150px; height: 100px; background-color: #").append(color).append("\"></div>\n")
					.append(" */\n")
					.append("public static final int ").append(name).append(" = 0x").append(color).append(";\n")
			;
			System.out.println(jf);
		}
	}

	@Test
	void genCss() throws Exception
	{
		var fields = CssColors.class.getFields();
		for(var field : fields)
		{
			var name = field.getName();
			var rgb = (int) field.get(null);


			var color = String.format("%06x", rgb).toUpperCase();

			var jf = new StringBuilder();

			jf.append("/**\n")
					.append(" * <span style=\"color: #").append(color).append("; font-family: Consolas; font-size: 36px\">").append(name).append("</span>\n")
					.append(" * <div style=\"width: 150px; height: 100px; background-color: #").append(color).append("\"></div>\n")
					.append(" */\n")
					.append("public static final int ").append(name).append(" = 0x").append(color).append(";\n")
			;
			System.out.println(jf);
		}
	}
}
