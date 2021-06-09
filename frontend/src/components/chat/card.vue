<template>
  <div id="card">
    <el-autocomplete
      class="inline-input"
      v-model="inputContent"
      :fetch-suggestions="querySearch"
      placeholder="搜索群或好友.."
      :trigger-on-focus="false"
      @select="handleSelect"
      prefix-icon="el-icon-search"
    >
      <ul slot-scope="{ item }" style="padding: 0px">
        <li style="padding: 0px">
          <span v-html="brightenKeyword(item.value)" :title="item.value"></span>
        </li>
      </ul>
    </el-autocomplete>
  </div>
</template>

<script>
import { mapState } from "vuex";
import Vue from "vue";
export default {
  data() {
    return {
      inputContent: "",
    };
  },
  computed: {
    ...mapState(["users", "groups"]),
  },
  methods: {
    querySearch(queryString, cb) {
      var users = this.users;
      users.forEach((item) => {
        item.value = item.nickname;
      });
      var groups = this.groups;
      groups.forEach((item) => {
        item.value = item.groupName;
      });

      var resultsUser = queryString
        ? users.filter(this.createFilter(queryString))
        : users;
      var resultsGroup = queryString
        ? groups.filter(this.createFilter(queryString))
        : groups;
      resultsUser.forEach((item) => {
        item.value = "好友：" + item.value;
      });
      resultsGroup.forEach((item) => {
        item.value = "群：" + item.value;
      });
      var results = resultsUser.concat(resultsGroup);
      cb(results);
    },
    createFilter(queryString) {
      return (item) => {
        return (
          item.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
        );
      };
    },
    handleSelect(item) {
      var vtmp = item.value;

      Vue.delete(item, "value");
      if ("groupName" in item) {
        this.$store.commit("changeCurrentList", "群聊");
        this.$store.commit("changeCurrentSessionGroup", item);
        //去掉前缀
        item.value = vtmp.slice(2);
      } else {
        this.$store.commit("changeCurrentList", "私聊");
        this.$store.commit("changeCurrentSession", item);
        //去掉前缀
        item.value = vtmp.slice(3);
      }
    },
    // 搜索关键字高亮
    brightenKeyword(val) {
      const Reg = new RegExp(this.inputContent, "i");
      if (val) {
        return val.replace(
          Reg,
          `<span style="color: #409EFF;">${this.inputContent}</span>`
        );
      }
    },
  },
};
</script>

<style lang="scss" scoped>
#card {
  padding: 12px;
  margin-bottom: 5px;
  .avatar {
    width: 40px;
    height: 40px;
    vertical-align: middle; /*这个是图片和文字居中对齐*/
  }
  .name {
    display: inline-block;
    padding: 10px;
    margin-bottom: 15px;
    font-size: 16px;
  }
  .search {
    background-color: #dbd9d8;
    width: 150px;
    height: 30px;
    line-height: 30px;
    padding: 0 10px;
    border: 0;
    border-radius: 4px;
    outline: none; /*鼠标点击后不会出现蓝色边框*/
    color: #fff;
  }
  .searchBtn {
    background-color: #dbd9d8;
    border: 0;
    margin-left: 3px;
  }
}
</style>
<style>
/*当前组件的el-input样式设置*/
#card .el-input__inner {
  background-color: #dbd9d8;
  outline: none; /*鼠标点击后不会出现蓝色边框*/
}
</style>
