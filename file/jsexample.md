#   取DIV元素底下的所有input子元素


```html
<!DOCTYPE html>
<html lang="en">

	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta http-equiv="X-UA-Compatible" content="ie=edge">
		<title>Document</title>
	</head>

	<body>
		<div id="divNode">
			<div>
				<input type="text" name="" id="" value="1">
				<input type="text" name="" id="" value="1">
				<input type="text" name="" id="" value="1">
				<input type="text" name="" id="" value="1">
			</div>
			<div>
				<input type="text" name="" id="" value="">
				<input type="text" name="" id="" value="">
				<input type="text" name="" id="" value="">
				<input type="text" name="" id="" value="">
			</div>
			<div>
				<input type="text" name="" id="" value="">
				<input type="text" name="" id="" value="q">
				<input type="text" name="" id="" value="">
				<input type="text" name="" id="" value="">
			</div>
			<div>
				<input type="text" name="" id="" value="a">
				<input type="text" name="" id="" value="">
				<input type="text" name="" id="" value="">
				<input type="text" name="" id="" value="">
			</div>
		</div>
	</body>
	<script>
		let node = document.getElementById('divNode')
		let attrNode = node.childNodes
		attrNode.forEach ((item, index) => {
			if(item.nodeName === 'DIV') {
				let inputElements = item.childNodes
				let attrFlag = false
				inputElements.forEach ((item, index1) => {//第一次回圈，判斷DIV區塊有沒有input被填值
					if(item.nodeName === 'INPUT') {
						if(item.value) {
							attrFlag = true
						}
					}
				})
				if (attrFlag) {//開始添加屬性
					inputElements.forEach ((item, index2) => {
						if(item.nodeName === 'INPUT') {
							item.setAttribute('apple', 'true')
						}
					})
				}

			}
		})
		console.log(node)
		console.log('a', a)
		let b = node.childNode;
		console.log('b', b)
	</script>

</html>
```