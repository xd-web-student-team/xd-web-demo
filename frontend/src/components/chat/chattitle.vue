<template>
  <div class="chatTitle">
    <span class="titleName" v-show="this.$store.state.currentList == '群聊'">{{this.$store.state.currentSessionGroup.groupName}}</span>
    <span class="titleName" v-show="this.$store.state.currentList == '私聊'">{{this.$store.state.currentSession.nickname}}</span>

    <el-popover
      placement="right"
      width="250"
      trigger="click">
      <el-button class="moreBtn" size="small" icon="el-icon-more" slot="reference"></el-button>
    </el-popover>
  </div>
</template>

<script>
  export default {
    name: "chattitle"
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
