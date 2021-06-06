<template>
  <div class="chatTitle">
    <span class="titleName" v-show="currentList == '群聊'">{{currentSessionGroup.groupName}}</span>
    <span class="titleName" v-show="currentList == '私聊'">{{currentSession.nickname}}</span>

    <el-popover
      placement="right"
      width="250"
      trigger="click">
      <el-button class="moreBtn" size="small" icon="el-icon-more" slot="reference" @click="loadGroupHolderName()"></el-button>
      <h2>
        群主：{{groupHolder.nickname}}
      </h2>
      <el-divider content-position="right"><i class="fa fa-sun-o" aria-hidden="true"></i></el-divider>
      <el-button type="danger" plain @click="quitGroup">退出群聊</el-button>
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
      }
    },
    computed: mapState([
      "groups",
      "sessions",
      "currentList",
      "currentSession",
      "currentSessionGroup",
    ]),
    methods: {
      loadGroupHolderName() {
        //获取群主名
        this.getRequest("user/selectOne?id=" + this.currentSessionGroup.idGroupHolder).then((resp) => {
          this.groupHolder = resp
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
      }
    },
  }
</script>

<style scoped>
  .chatTitle{
    height: 50px;
    width: 100%;
    font-size: 20px;
    display: flex;
    justify-content: space-between;
    border-bottom: 0.5px solid #c7d2db;
  }
  .moreBtn{
    background-color: #eee;
    border: 0;
    height: 40px;
  }
  .titleName{
    margin: 10px 20px;
  }

</style>
