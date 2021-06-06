<template>
<div id="message" v-scroll-bottom="sessions">
	<!-- <div v-show="currentList == '私聊'">
      <ul>
        <li
          v-for="entry in sessions[
            user.username + '#' + currentSession.username
          ]" :key="entry"
        >
          <p class="time">
            <span>{{ entry.date | time }}</span>
          </p>
          <div class="main" :class="{ self: entry.self }">
            <p class="username">{{ entry.fromNickname }}</p>
            <img
              class="avatar"
              :src="entry.self ? user.userProfile : currentSession.userProfile"
              alt=""
            />
            <p v-if="entry.messageTypeId == 1" class="text">
              {{ entry.content }}
            </p>
            <img
              v-if="entry.messageTypeId == 2"
              :src="entry.content"
              class="img"
            />
          </div>
        </li>
      </ul>
    </div> -->
    <!-- 群聊 -->
    <div v-show="currentList == '群聊' && currentSessionGroup != null">
      <div v-if="sessions['群聊'][currentSessionGroup.id] == null" style="text-align:center; padding: 40% 0 0 0">暂无消息</div>
      <div v-else>
        <ul>
          <li v-for="entry in sessions['群聊'][currentSessionGroup.id].groupMsgContentList" :key="entry.id">
            <p class="time" v-show="updateLastTime(entry.createTime)">
              <span>{{ entry.createTime | time }}</span>
            </p>
            <div class="main" :class="{ self: entry.fromId == user.id }">
              <p class="username">{{ entry.fromName }}</p>
              <img
                @dblclick="takeAShot"
                class="avatar"
                :src="
                  entry.fromId == user.id ? user.userProfile : entry.fromProfile
                "
                alt=""
              />
              <div v-if="entry.messageTypeId == 1">
                <p class="text" v-html="entry.content"></p>
              </div>
              <div v-else>
                <el-image
                  :src="entry.content"
                  :preview-src-list="[entry.content]"
                  class="img"
                >
                  <div slot="error" class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </div>
    
  </div>
</template>

<script>
import { mapState } from "vuex";
//判断时间用
let lastTime = new Date();

export default {
  name: "message",
  data() {
    var validateGroupName = (rule, value, callback) => {
      //检查群名是否重复
      this.getRequest("chat/checkGroupName?groupName=" + value).then((resp) => {
        if (resp != 0) {console.log("116")
          callback(new Error("该群名已被注册"));
        } else {console.log("118")
          callback();
        }
      });
    };
    var validateJoinGroup = (rule, value, callback) => {
      if (value == "") {
        callback(new Error("请输入群聊名称"))
      }
      //检查群名是否存在
      this.getRequest("chat/checkGroupName?groupName=" + value).then((resp) => {
        if (resp == 0) {
          callback(new Error("该群不存在"));
        }else {
          callback();
        }
      });
    };
    return {
      user: JSON.parse(window.sessionStorage.getItem("user")),
      groupForm: {
        groupName: '',
      },
      groupRules: {
        groupName: [{ validator: validateGroupName, trigger: "blur"},
                    { required: true, message: "请输入群聊名称", trigger: "blur"}],
      },
      joinGroupRules: {
        groupName: [{ validator: validateJoinGroup, tirgger: "blur"}],
      }
    };
  },
  computed: mapState([
    "sessions",
	  "currentList",
    "forthService",
    "currentUser",
    "currentSession",
    "currentSessionGroup",
    "errorImgUrl",
  ]),
  filters: {
    time(date) {
      date = new Date(date);
      //当前的时间
      let currentDate = new Date();

      //与当前时间的日期间隔
      let timeInterval = currentDate.getDate() - date.getDate();

      //星期数组
      let weekdays = [
        "星期日",
        "星期一",
        "星期二",
        "星期三",
        "星期四",
        "星期五",
        "星期六",
      ];

      //时间范围
      let timeRange = "上午";
      if (date.getHours() > 12) {
        timeRange = "下午";
      }

      //如果与当前时间同日
      if (
        date.getMonth() == currentDate.getMonth() &&
        date.getDate() == currentDate.getDate()
      ) {
        return `${timeRange} ${date.getHours()}:${date.getMinutes()}`;
      }

      //在当前时间同一年且日期间隔在7天内
      if (
        date.getFullYear() == currentDate.getFullYear() &&
        timeInterval <= 6 &&
        timeInterval >= 1
      ) {
        //当前时间的前一天
        if (timeInterval == 1) {
          return `昨天 ${timeRange} ${date.getHours()}:${date.getMinutes()}`;
        } else {
          return `${
            weekdays[date.getDay()]
          } ${timeRange} ${date.getHours()}:${date.getMinutes()}`;
        }
      }

      //如果日期超过7天
      else
        return `${date.getFullYear()}-${
          date.getMonth() + 1
        }-${date.getDate()} ${timeRange}  ${date.getHours()}:${date.getMinutes()}`;
    },
  },
  directives: {
    /*这个是vue的自定义指令,官方文档有详细说明*/
    // 发送消息后滚动到底部,这里无法使用原作者的方法，也未找到合理的方法解决，暂用setTimeout的方法模拟
    "scroll-bottom"(el) {
      //console.log(el.scrollTop);
      setTimeout(function () {
        el.scrollTop += 9999;
      }, 1);
    },
  },
  methods: {
    takeAShot(fromName, toName) {
      console.log("拍了一怕");
      let s = fromName + "拍了拍" + toName;
    },

    //判断时间间隔以决定是否输出时间span
    updateLastTime(date) {
      let newDate = new Date(date);
      //近两分钟不打印时间
      if (
        newDate.getFullYear() == lastTime.getFullYear() &&
        newDate.getMonth() == lastTime.getMonth() &&
        newDate.getDate() == lastTime.getDate() &&
        newDate.getHours() == lastTime.getHours()
      ) {
        let m1 = newDate.getMinutes();
        let m2 = lastTime.getMinutes();
        lastTime = newDate;
        if (m1 >= m2) {
          return m1 - m2 > 2;
        } else return 60 - m1 + m2 > 2;
      }
      lastTime = newDate;
      return true;
    },
    //建群
    buildGroup() {
      this.$refs.groupForm.validate((valid) => {
        console.log(valid)
        if (valid) {
          this.postKeyValueRequest("/chat/buildGroup?groupName=" + this.groupForm.groupName).then((resp) => {
            this.$message({
              message: '创建成功',
              type: 'success'
            });
          });
        } else {
          this.$message.error("请正确填写信息！")
          return false;
        }
      })
    },
    //加群
    joinGroup() {
      this.$refs.groupForm.validate((valid) => {
        if (valid) {
          this.postKeyValueRequest("/chat/joinGroup?groupName=" + this.groupForm.groupName).then((resp) => {
            this.$message({
              message: '加入成功',
              type: 'success'
            });
          });
        }
      })
    },
    //加好友
    beFriend() {

    },
  },
};
</script>

<style lang="scss" scoped>
#message {
  padding: 15px;
  height: 60%;
  max-height: 63%;
  overflow-y: scroll;
  overflow-x: hidden;
  ul {
    list-style-type: none;
    padding-left: 0px;
    li {
      margin-bottom: 15px;
    }
  }
  .time {
    text-align: center;
    margin: 7px 0;
    > span {
      display: inline-block;
      padding: 0 18px;
      font-size: 12px;
      color: #fff;
      background-color: #dcdcdc;
      border-radius: 2px;
    }
  }
  .main {
    .avatar {
      float: left;
      margin: 0 10px 0 0;
      border-radius: 3px;
      width: 30px;
      height: 30px;
    }
    .text {
      display: inline-block;
      padding: 0 10px;
      max-width: 80%;
      background-color: #fafafa;
      border-radius: 4px;
      line-height: 30px;
    }
    .img {
      display: inline-block;
      height: 100px;
      width: 100px;
      margin-top: 15px;
    }
    .username {
      position: relative;
      left: 35px;
      top: 11px;
      margin: 0 0;
      padding: 0 0;
      border-radius: 4px;
      line-height: 15px;
      font-size: 10px;
      color: grey;
    }
  }
  .self {
    text-align: right;
    .avatar {
      float: right;
      margin: 0 0 0 10px;
      border-radius: 3px;
      width: 30px;
      height: 30px;
    }
    .text {
      display: inline-block;
      padding: 0 10px;
      max-width: 80%;
      background-color: #b2e281;
      border-radius: 4px;
      line-height: 30px;
    }
    .img {
      display: inline-block;
      height: 100px;
      width: 100px;
      margin-top: 15px;
    }
    .username {
      //display: inline-block;
      position: relative;
      left: 310px;
      top: 11px;
      margin: 0 0;
      padding: 0 0;
      width: 200px;
      line-height: 15px;
      font-size: 10px;
      color: grey;
    }
  }
  .inputBox {
    margin-top: 20%;
    margin-bottom: 20px;
    border-width: 5px;
    border-radius: 10px;
    border-color: rgb(113, 71, 54);
    height: 30px;
    width: 250px;
  }
}
</style>
