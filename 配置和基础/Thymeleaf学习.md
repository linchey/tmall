# Thymeleaf

## 1.简介

Thymeleaf的主要目标是提供一个优雅和高度可维护的创建模板的方式。为了实现这一点，它建立在*自然*模板的概念上，将其逻辑注入到模板文件中，不会影响模板被用作设计原型。这改善了设计的沟通，弥合了设计和开发团队之间的差距。

比Jsp和Freemarker的优势，一般的模板技术都会在页面加各种表达式、标签甚至是java代码，而这些都必须要经过后台服务器的渲染才能打开。

但如果前端开发人员做页面调整，双击打开某个jsp或者ftl来查看效果，基本上是打不开的。

那么Thymeleaf的优势就出来了，因为Thymeleaf没有使用自定义的标签或语法，所有的模板语言都是扩展了标准H5标签的属性

比如

```
<div th:text="${item.skuName} "></div>`
```

它的效果和Jsp中的

```
 <div>${item.skuName}</div>
```

渲染后效果一样，但是如果你直接用浏览器打开页面文件，H5会把th:text这种不认识的属性忽略掉。效果就和<div></div> 没有区别，所以对于前端调页面影响更新。以上只是举了一个例子，如果是循环、分支的判断效果更明显。

## 2.快速入门

### 2.1、添加头文件

   就行Jsp的<%@Page %>一样 ，Thymeleaf的也要引入标签规范。不加这个虽然不影响程序运行，但是你的idea会认不出标签，不方便开发。

```
<html lang="en" xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
```

### 2.2、取值

```html
<!--一般取值-->
<span th:text="${hello}">打底值</span>
<!--循环取集合里值-->
方式一：
<p th:each="str:${list}" th:text="${str}" ></p>
方式二：
<span th:each="str:${list}">
    <span th:text="${str}"></span>
</span>
```

### 2.3、switch

```html
<td th:switch="${status}">
	<span th:case="0">否</span>
	<span th:case="1">是</span>
</td>
```

### 2.4、if

```
<span th:if="${check}==1" th:text="男"></span>
<span th:if="${check==1}" th:text="男"></span>
```

> 两种虽然结果都能运行，但前者是html允许支持的，后者是thymeleaf支持的

大于号 gt(great than)

小于号 lt(less than)

### 2.5、三目运算

```
<span th:text="${check}==1? '男':'女'"></span>
<span th:text="${check==1}? '男':'女'"></span>
```

### 2.6、引用页面

```
  <div th:include="itemInner"/>  
  
  <!--引用页面的部分代码-->
    <div th:include="itemInner::"/>  
```

处理电话号码

```html
<span th:text="${#strings.substring(userphone,0,3)+'****'+#strings.substring(userphone,7,11)}"></span>

```



渲染后效果一样，但是如果你直接用浏览器打开页面文件，H5会把th:text这种不认识的属性忽略掉。效果就和<div></div> 没有区别，所以对于前端调页面影响更新。以上只是举了一个例子，如果是循环、分支的判断效果更明显。	

