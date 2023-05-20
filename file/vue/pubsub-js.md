# pubsub-js組件間傳值

此API可以用於 同階層組件間 、 父子組件間 、父子孫組件間...等不受階層限制的傳值

step1:安裝組件

npm install --save pubsub-js

step2:訂閱消息(綁定監聽)

PubSub.subscribe('msg', function(msg, data){})

step3:發送消息(觸發事件)

PubSub.publish('msg', data)
