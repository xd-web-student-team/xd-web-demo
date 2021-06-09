<template>
  <div class="chatTitle">
    <span class="titleName" v-show="currentList == '群聊'">{{currentSessionGroup.groupName}}</span>
    <span class="titleName" v-show="currentList == '私聊'">{{currentSession.nickname}}</span>

    <el-popover placement="right" trigger="click" v-show="currentList == '群聊'">
      <el-button class="moreBtn" size="small" icon="el-icon-more" slot="reference" @click="loadGroupMember()"></el-button>
      <h2>群主：{{groupHolder.nickname}}</h2>
      <el-divider content-position="right"><i class="fa fa-sun-o" aria-hidden="true"></i></el-divider>
      <h3>成员：</h3>
      <div style="overflow-y: scroll; height:240px;">
        <!-- 群主 -->
        <ul v-if="groupHolder.id == currentUser.id">
          <li v-for="item in users" :key="item.id" style="height:35px;">
            <div style="display:inline-block; width:60%">{{item.nickname}}</div>
            <div style="display:inline-block;"><el-button type="danger" plain @click="kickGroup(item)" size="mini">踢出群聊</el-button></div>
          </li>
        </ul>
        <!-- 非群主 -->
        <ul v-else>
          <li v-for="item in users" :key="item.id" style="height:35px;">
            <div style="display:inline-block; width:60%">{{item.nickname}}</div>
            <div v-show="item.id == currentUser.id" style="display:inline-block; color:rgb(145, 112, 93)">我</div>
          </li>
        </ul>
      </div>
      <el-button type="danger" plain @click="quitGroup" style="margin-top:20px;">退出群聊</el-button>
    </el-popover>
  </div>
</template>

<script>
  import { mapState } from "vuex";
  import Vue from 'vue';
  export default {
    name: "chattitle",
    data() {
      return {
        groupHolder: {
          nickname: '',
        },
        users:[
          {id:0, nickname:'default'},
        ]
      }
    },
    computed: mapState([
      "stomp",
      "groups",
      "sessions",
      "currentUser",
      "currentList",
      "currentSession",
      "currentSessionGroup",
    ]),
    methods: {
      loadGroupMember() {
        //获取群主名
        this.getRequest("user/selectOne?id=" + this.currentSessionGroup.idGroupHolder).then((resp) => {
          this.groupHolder = resp
        });
        //获取群成员
        this.postRequest("user/getGroupMembers", this.currentSessionGroup).then((resp) => {
          this.users=resp
        });
      },
      //退出群聊
      quitGroup() {
        this.$confirm('你将退出该群聊, 是否继续?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.postRequest("/chat/quitGroup", this.currentSessionGroup).then(() => {
            //删除消息
            this.groups.splice(this.groups.indexOf(this.currentSessionGroup),1);
            Vue.delete(this.sessions['群聊'], this.currentSessionGroup.id);
            this.currentSessionGroup = null;

            this.$message({
              type: 'success',
              message: '莫愁前路无知己，天下谁人不识君'
            });
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消退群'
          });          
        });
      },
      //踢出群聊
      kickGroup(user) {
        this.$confirm('你将踢出该群友：'+user.nickname+', 是否继续?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let msgObj = new Object();
          // 踢人需要： 用户id 群：id 名 群主
          msgObj.from = user.id;
          msgObj.content = this.currentSessionGroup.id;
          msgObj.fromNickname = this.currentSessionGroup.groupName;
          msgObj.messageTypeId = this.currentSessionGroup.idGroupHolder;

          //转发需要
          msgObj.to = this.currentSession.username;

          this.stomp.send("/ws/kickGroup", {}, JSON.stringify(msgObj));
          //删除消息
          this.users.splice(this.users.indexOf(user),1);
          this.$message({
            type: 'success',
            message: '梅落繁枝千万片，犹自多情，学雪随风转。'
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消操作'
          });          
        });
      }
    },
  }
</script>

<style lang="scss">
/* el-popover是和app同级的，所以scoped的局部属性设置无效 */
/* 需要设置全局style */
.el-popover {
  height: 450px;
  width: 300px;
  overflow: scroll;
  
}
</style>

<style scoped>
  ::-webkit-scrollbar {
    width: 0 !important;
  }
  .chatTitle{
    height: 50px;
    width: 100%;
    font-size: 20px;
    display: flex;
    justify-content: space-between;
    border-bottom: 0.5px solid #c7d2db;
  }
  .moreBtn{
    background-color: rgba(238, 238, 238, 0);
    border: 0;
    height: 40px;
  }
  .titleName{
    margin: 10px 20px;
  }

</style>
