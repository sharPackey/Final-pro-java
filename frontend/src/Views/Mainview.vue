<template>
  <div>
    <el-tabs type="border-card" class="el-tabs" @tab-click="handleClick">
      <el-tab-pane label="Number of Answers" name="Appointment"
      >
        <F_T></F_T>
      </el-tab-pane>
      <el-tab-pane label="Accepted Answers" name="lookAppoint"
      >
        <S_T></S_T>
      </el-tab-pane>
      <el-tab-pane label="Tags" name="Post"
      >
        <T_T></T_T>
      </el-tab-pane>
      <el-tab-pane label="Users" name="MyPost"
      >
        <Fo_T></Fo_T>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import F_T from "@/components/First_Three.vue"
import S_T from '@/components/Second_Three.vue'
import T_T from '@/components/Third_T.vue'
import Fo_T from '@/components/Four_T.vue'

export default {
  name: "MainView",
  components: {
    F_T,
    S_T,
    T_T,
    Fo_T
  }
}
</script>


<style scoped>
.common-layout {
  padding: 0;
  margin: 0;
  height: 100%;
}

.admin-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
}

.myAside {
  height: 100%;
  background-color: #545c64;
}

.myHeader {
  width: 100%;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 0 0 #ececec;
}

.parent {
  /* position: relative; */
}

.el-dropdown {
  position: absolute;
  right: 0%;
  /* 其他样式 */
}

.button {
  position: absolute;
  right: 9%;
}

.el-tabs {
  flex: 1;
  /* 其他样式 */
}
</style>