package com.fagougou.government.utils

object Handlebars {
    val templete = """
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
</head>
<body>
<script id="myTemplate" type="text/x-handlebars-template">
    {{TemplateHook}}
</script>
<div id="box"></div>
<script>
  var source = ${'$'}("#myTemplate").html();
  var template = Handlebars.compile(source);
  var context = {{{DataHook}}};
  var html = template(context);
  ${'$'}('#box').html(html);
</script>
</body>
</html>
"""
}