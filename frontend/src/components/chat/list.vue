<template>
  <div id="list">
    <el-scrollbar
      wrap-class="userList"
      wrap-style="height:600px;"
      view-style="height:100%;"
      :native="false"
    >
      <!--群聊列表-->
      <ul v-if="currentList == '群聊'">
        <p style="padding: 2px 4px; height: 20px">群聊列表</p>
        <li
          v-for="item in groups"
          v-on:click="changeCurrentSessionGroup(item)"
          v-bind:key="item.id"
        >
          <div style="display: flex; justify-content: space-between">
            <div>
              <el-badge>
                <el-image
                  class="avatar"
                  :src="require('../../assets/image/group.png')"
                >
                  <div slot="error" class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
              </el-badge>
              <p class="name">{{ item.groupName }}</p>
            </div>
          </div>
        </li>
      </ul>

      <!--好友列表-->
      <ul v-if="currentList == '私聊'">
        <p style="padding: 2px 4px; height: 20px">好友列表</p>
        <li
          v-for="item in users"
          :class="{
            active: currentSession
              ? item.username === currentSession.username
              : false,
          }"
          v-on:click="changeCurrentSession(item)"
          v-bind:key="item.id"
        >
          <!--   :class="[item.id === currentSession ? 'active':'']" -->
          <div style="display: flex; justify-content: space-between">
            <div>
              <el-badge
                :is-dot="isDot[user.username + '#' + item.username]"
                style=""
              >
                <el-image
                  class="avatar"
                  :preview-src-list="[item.userProfile]"
                  :src="item.userProfile"
                  :alt="item.nickname"
                >
                  <div slot="error" class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
              </el-badge>
              <p class="name">{{ item.nickname }}</p>
            </div>
            <div>
              <el-badge
                :value="item.userStateId == 1 ? '在线' : '离线'"
                :type="item.userStateId == 1 ? 'danger' : 'info'"
              ></el-badge>
            </div>
          </div>
        </li>
      </ul>
    </el-scrollbar>

    <!-- 待做 -->
    <!-- <li
        :class="{
          active: currentSession
            ? '西电计科院交流群' == currentSession.username
            : false,
        }"
        v-on:click="changeCurrentSession(chatObj2)"
      >
        <img
          class="avatar"
          src="https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1268761962,3976237305&fm=26&gp=0.jpg"
        />
        <el-badge :is-dot="isDot[user.username + '#西电计科院交流群']">
          <p class="name">西电计科院交流群</p>
        </el-badge>
      </li>
    </ul> -->
    <!--机器人-->
    <!-- <ul v-if="currentList == '机器人'">
      <p style="padding: 2px 4px; height: 20px">快来和机器人聊天吧！</p>
      <li
        :class="{
          active: currentSession ? '机器人' == currentSession.username : false,
        }"
        v-on:click="changeCurrentSession(robotObj)"
      >
        <img
          class="avatar"
          src="https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2548892998,499717296&fm=26&gp=0.jpg"
        />
        <p class="name">智能回复机器人</p>
      </li>
    </ul> -->
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "list",
  data() {
    return {
      user: this.$store.state.currentUser,
      /* robotObj: {
        username: "机器人",
        nickname: "机器人",
        userProfile:
          "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2548892998,499717296&fm=26&gp=0.jpg",
      }, */
    };
  },
  computed: mapState([
    //'sessions',//this.sessions映射成this.$store.state.sessions
    "users",
    "groups",
    "currentSession",
    "currentSessionGroup",
    "isDot",
    "currentList",
  ]),
  methods: {
    changeCurrentSession: function (currentSession) {
      this.$store.commit("changeCurrentSession", currentSession);
    },
    changeCurrentSessionGroup: function (currentSession) {
      this.$store.commit("changeCurrentSessionGroup", currentSession);
    },
  },
};
</script>

<style lang="scss" scoped>
#list {
  ul {
    padding-left: 0px;
    margin-left: 4px;
  }
  li {
    padding-top: 14px;
    padding-bottom: 14px;
    //padding-right: 40px;
    //border-bottom: 1px solid #292C33;
    list-style: none;
    cursor: pointer;
    &:hover {
      background-color: #d8d6d6;
    }
  }
  li.active {
    /*注意这个是.不是冒号:*/
    background-color: #c8c6c6;
  }
  .avatar {
    border-radius: 2px;
    width: 30px;
    height: 30px;
    vertical-align: middle;
  }
  .name {
    display: inline-block;
    margin-left: 15px;
    margin-top: 0px;
    margin-bottom: 0px;
  }
  .stateItem {
    //在线状态的样式
    /*position: absolute;*/
    /*left: 160px;*/
    //margin-left: 100px;
    //margin-right: 10px;
  }
  .userList {
    max-height: 600px;
  }
  .el-scrollbar__wrap.default-scrollbar__wrap {
    overflow-x: auto;
  }
}
</style>
