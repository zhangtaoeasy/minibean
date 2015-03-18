![http://dl.iteye.com/upload/picture/pic/126755/ec45e659-84ea-3fe3-bb07-8759a95838e8.jpg](http://dl.iteye.com/upload/picture/pic/126755/ec45e659-84ea-3fe3-bb07-8759a95838e8.jpg)
# **什么是MiniBean组件？** #
MiniBean是一个完全采用Java语言编写的Beans属性自动赋值注入组件。那么什么是Beans属性元素之间的自动赋值呢？假设我们在实际开发过程中，经常需要将N个不同类型的Bean之间的属性数据进行共享（这种情况多见于Reuqest Bean与Entity Bean之间，因为我们不可能直接用Entity Bean接受并存储请求参数），这个时候我们一般也就是采用手动方式进行，按照串行结构从上至下将需要共享的属性进行赋值操作。或许当Beans之间的属性元素不多时，这种做法还能勉强应付。但在实际的开发过程中，尤其是在企业级的项目或者产品中，往往一个Bean中所定义的属性元素非常多，这个时候再采取手动赋值的方式将会是一场灾难。而诸多企业也考虑到这个问题所带来的诸多弊端，从而几乎都编写有适用于自身场景的特定组件。但是这些组件大致都存在一些不规范的问题，比如其中最不可理喻的就是Beans之间必须保持相同的属性名称。我们都知道在实际的开发过程中，不同的Bean可能由于业务的不同，所定义的元素名称也不尽相同，所以要求Beans之间所有需要进行自动赋值的属性元素都保持相同的名称，这几乎是不可能的。

# **MiniBean的特性** #
MiniBean除了可以很好的解决上述提及的一些问题外，还能够带给企业项目组的更多实惠。MinBean采用的是完全轻量级的架构设计方案。在保持对Java API的薄层封装时，更注重本身结构的扩展性和伸缩性。并且在业务代码中使用MiniBean时，你完全不必担心MiniBean会对你的代码产生过多耦合，因为MiniBean本身并不会直接影响代码语意。并且MiniBean遵循的是Apache License2的开源协议，你甚至完全可以根据实际情况对MiniBean的源码进行二次开发（MiniBean采用的是Maven3.x工具进行项目的构建管理和依赖管理，如果你本身并不熟悉Maven，那么你首先需要了解该工具的一些使用方式），以此满足你所需要的特定应用场景。
MiniBean不仅可以满足Beans之间的属性元素名称不统一的情况，还可以允许你在目标Bean中为属性元素定义一个或者N个备用名称组来满足实际情况。而且MiniBean的效率也是非常高效的，在实际压力测试中，Beans之间需要实现自动赋值的数据容量高达<=500mb时，MiniBean仅耗时<=10ms，由此可见MiniBean完全可以胜任一些大型的数据赋值应用场景。

MinBean的下载地址为：http://gao-xianglong.iteye.com/blog/1914643

# **MiniBean的基本使用方式** #
## **1、使用MiniBean来构建自动赋值应用场景** ##
在开始在你的代码中使用MinBean之前，我们首先应该下载MiniBean所需的构件。值得庆幸的是，MiniBean并没有依赖除了Java API之外的其他构件，也就是说使用MiniBean你将完全不必下载其他的第三方构件。MiniBean目前的最新版本是2.0.1，该版本目前较为稳定，单元测试覆盖率已经达到85%以上，完全可以满足RC版本的要求。
当成功下载好MiniBean的主要构件后，我们需要将它引入至项目中。然后我们接下来看看如何在程序中使用MiniBean来实现Beans属性元素之间的自动赋值。我们首先应该预先定义好2个Bean（分别为目标对象与源对象）。

定义源对象Bean：
```
public class TestBeanA {
	private int id;
	private String name = null;
	private String pwd = null;
        /* 此处省略set和get方法 */
}
```

定义目标对象Bean：
```
@Assignment
public class TestBeanB {
	@Paramater
	private int id;

	@Paramater
	private String name;

	@Paramater
	private String pwd;
        /* 此处省略set和get方法 */
}
```

当成功定义好目标对象Bean与源对象Bean以后，我们首先来看看目标对象的类型的上方，标记了一个注解叫做@Assignment。该注解用于标记目标对象Bean，其作用域为类型,只有标记了该注解的Bean才能实现后续的自动赋值操作。而@Paramater注解则用于标记在赋值过程中需要被源对象赋值的目标对象中的指定字段，只有在字段的上方标记有@Paramater注解时，MiniBean才会对其进行赋值注入。@Paramater除了允许标记在字段上，还允许标记在方法上，也就是说@Paramater注解的作用域是字段与方法。

接下来我们使用MiniBean提供的BeanContext组件实现Beans之间的自动赋值操作：
```
@Test
public void test1() {
	TestBeanA sourceObject = new TestBeanA();
	sourceObject.setId(1);
	sourceObject.setName("JohnGao");
	sourceObject.setPwd("123456");

	TestBeanB goalObject = new TestBeanB();
        
        /* 实现自动赋值操作 */
	BeanContext.setParam(goalObject, sourceObject);

        /* 单元测试预期值与实际值是否匹配 */
	Assert.assertEquals(goalObject.getId(), sourceObject.getId());
	Assert.assertEquals(goalObject.getName(), sourceObject.getName());
	Assert.assertEquals(goalObject.getPwd(), sourceObject.getPwd());
}
```

上述程序示例我们可以看出，BeanContext类型是作为整个Beans赋值操作的入口容器，由该类型负责将目标对象与实际对象之间数据进行共享牵引。该方法对外包含一个公共方法setParam(goalObject, sourceObject)，该方法的第一个参数用于传递目标对象，而后一个用于传递源对象。所以大家在使用时一定要注意参数传递的顺序，切勿将顺序反转，以免发生一些不必要的错误。

## **2、使用@Paramater定义目标对象字段的备用名称与备用名称组** ##
在实际的开发过程中，我们不可能要求Beans均保持相同的字段名称，因为这需要根据实际的业务需求而定。所以MiniBean考虑到了这一点，为开发人员提供有字段的备用及备用名称组功能供其使用。如果我们需要在目标对象的字段定义备用名称，则需要使用@Paramater注解的value、name或names属性。value属性用于定义目标对象字段的备用名称，而name属性同样也用于备用名称定义，只不过value属性是缺省的，而name属性则是显式定义的，除此之外没有其他区别。也就是说在程序中如果显示定义有name属性则重写value属性的定义。而names属性则用于定义字段的备用名称组，也就是说一个字段可以拥有N个备用名称，在运行时，MiniBean会根据源对象字段的名称匹配备用名称组中对应的名称。

定义目标对象字段的备用名称：
```
@Assignment
public class TestBeanD {
	@Paramater("id")
	private int userId;

	@Paramater(name = "name")
	private String userName;

	@Paramater(names = { "pwd", "pwd2", "pwd3" })
	private String passWord;
        /* 此处省略set和get方法 */
}
```

从上述程序示例中，我们使用了@Paramater中包含的3种有效属性来定义目标对象字段的备用名称和备用名称组。值得注意的是，value属性和name属性的类型都是String类型，只有names属性是String[.md](.md)类型，所以大家在定义属性名称的时候一定要注意。

## **3、使用@AutoParameters实现目标对象所有字段的赋值注入** ##
任何事情都没有万一的，如果Beans之间的字段名称都是相同的时候，还采取@Paramater的方式进行定义则显得过于繁琐。所以MiniBean提供有@AutoParameters注解用于标记目标对象中所有的字段都允许被源对象赋值注入。@AutoParameters注解的作用域仅限于类型，所以千万不要将该注解标记与字段或者方法上，否则编译时将会出现错误。并且一旦在程序中使用了@AutoParameters注解，我们完全可以去除掉字段上的@Paramater注解，当然@Paramater和@AutoParameters注解也可以混合使用，以此满足一些特定的应用场景。

使用@AutoParameters和@Paramater混合字段注入：
```
@Assignment
@AutoParameters
public class TestBeanF {
	private int id;

	@Paramater("name")
	private String userName;

	@Paramater(names = { "pwd1", "pwd2", "pw3"})
	private String passWord;
        /* 此处省略set和get方法 */
}
```

在@AutoParameters注解内部，定义有一个缺省的value属性。该属性缺省值为“true”，也就是说@AutoParameters可以生效，一旦value属性为“false”的时候，@AutoParameters则失效。一点@AutoParameters失效后，目标对象中如果没有标记@Paramater的字段MiniBean将不再为其提供赋值注入。

@AutoParameters失效：
```
@AutoParameters(false)
```

## **4、使用ignore属性定义字段名称的大小写兼容** ##
或许在某些应用环境下，我们往往要考虑相同的字段名称，但大小写却不一致的情况下的自动赋值操作。Java是一门强类型的语言，不仅仅会检查类型的兼容，同样变量名称的大小写也是区分的。所以这个时候，如果使用MiniBean进行Bean之间的自动赋值操作，我们就需要考虑有什么办法可以做到相同的字段名，但大小写不一致的情况，仍然能够实现自动赋值操作。MiniBean在@Paramater和@AutoParameters注解内部提供有ignore属性供开发人员选择，该属性的作用就是用于定义是否兼容字段的大小写，缺省情况下ignore的值为“false”，即不开启大小写兼容，反之则为开启。

使用ignore属性开启字段名称的大小写兼容：
```
@Assignment
@AutoParameters(ignore = true)
public class TestBeanK {
	private int Id;

	@Paramater
	private String name;

	@Paramater(ignore = true)
	private String Pwd;
	/* 此处省略set和get方法 */
}
```

## **5、一对多的自动赋值操作** ##
在前面几个章节中，我们更多演示的是如果使用MiniBean进行目标对象和源对象之间的一对一自动赋值操作。那么如果我们有多个目标对象都需要与源对象之间进行自动赋值操作的时候，MiniBean是否提供有良好的支持呢？值得庆幸的是MiniBean提供有高效、便捷的一对多赋值策略实现一个源对象与多个目标对象之间的自动赋值操作。
我们可以使用BeanContext抽象类的setParams(Set goalObjects, K sourceObjects)方法实现一对多的自动赋值操作。和setParam方法不同的是，setParams方法的第一个参数为目标对象集合，也就是说开发人员只需要将需要实现自动赋值的目标对象集合存储进一个Set列表中即可完成一对多的自动赋值操作。

使用一对多的赋值策略：
```
@Test
public void test11() {
	TestBeanA sourceObject = new TestBeanA();
	sourceObject.setId(1);
	sourceObject.setName("JohnGao");
	sourceObject.setPwd("123456");

	Set goalObjects = new HashSet();
	TestBeanK testBeanK = new TestBeanK();
	TestBeanL testBeanL = new TestBeanL();
	goalObjects.add(new TestBeanK);
	goalObjects.add(new TestBeanL);
	BeanContext.setParams(goalObjects, sourceObject);
}
```

# **后记** #
当我们明确MiniBean的基本使用方式后，你就可以将MiniBean加入你的项目代码之中。这对于开发者来说不仅仅是一种实惠，并且让你更专注于你的业务，把这些繁琐的赋值操作全部统统交给MiniBean去替你完成。
最后不得不说的是，MiniBean随时随地都保持的新版本迭代更新，或许MiniBean还需要具备更多更有特色和实用功能，当然这需要你和其他人的不懈努力。所以当你熟练使用MiniBean后，不妨通过SVN下载MiniBean的源代码，跟作者一起对MiniBean注入更多新鲜血液。

如果你担心和怀疑MiniBean的稳定性，那么这会是你错误的一个认知，就2.0.1版本而言，MiniBean的核心代码单元测试覆盖率已经达到81.1%。所以请抛弃到你的猜疑，在你的项目中大胆的启动MiniBean来完成这些繁琐的自动赋值操作吧。

MiniBean核心代码的单元测试覆盖率示图：
![http://dl.iteye.com/upload/picture/pic/127851/4b018225-87f5-34ad-bee2-7a9b4869a3dd.jpg](http://dl.iteye.com/upload/picture/pic/127851/4b018225-87f5-34ad-bee2-7a9b4869a3dd.jpg)