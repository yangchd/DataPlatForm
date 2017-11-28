<template>
  <div id="carousel-single">
    <el-carousel :interval="5000" arrow="hover" height="400px">
      <el-carousel-item v-for="item in 6" :key="item">
        <h3>{{ item }}</h3>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>

</script>

<style>
  .el-carousel__item h3 {
    text-align: center;
    color: #475669;
    font-size: 100px;
    /*opacity: 0.75;*/
    line-height: 400px;
    margin: 0;
  }

  .el-carousel__item:nth-child(3n) {
    background-color: #20A0FF;
  }

  .el-carousel__item:nth-child(3n+1) {
    background-color: #13CE66;
  }

  .el-carousel__item:nth-child(3n+2) {
    background-color: #1F2D3D;
  }
</style>

