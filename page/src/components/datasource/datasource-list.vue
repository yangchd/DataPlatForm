<template>
  <div id="datasource-list">
    <el-table
      :data="tableData" border="true" stripe size="medium"
      maxHeight="400" >
      <el-table-column
        prop="datasource_name"
        label="数据源名称"
        width="180">
      </el-table-column>
      <el-table-column
        prop="url"
        label="数据源地址">
      </el-table-column>
      <el-table-column
        prop="user"
        label="用户"
        width="180">
      </el-table-column>
      <el-table-column  label="操作" width="300px">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleTest(scope.$index, scope.row)">测试
          </el-button>
          <el-button
            size="mini"
            @click="handleEdit(scope.$index, scope.row)">编辑
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)">删除
          </el-button>
        </template>
      </el-table-column>

    </el-table>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        tableData: [{
          datasource_name: '数据库aaa',
          url: '…………………………',
          user: '杨昌迪'
        },{
          datasource_name: '数据库aaa',
          url: '…………………………',
          user: '杨昌迪'
        },{
          datasource_name: '数据库aaa',
          url: '…………………………',
          user: '杨昌迪'
        },{
          datasource_name: '数据库aaa',
          url: '…………………………',
          user: '杨昌迪'
        },{
          datasource_name: '数据库aaa',
          url: '…………………………',
          user: '杨昌迪'
        },{
          datasource_name: '数据库aaa',
          url: '…………………………',
          user: '杨昌迪'
        },{
          datasource_name: '数据库aaa',
          url: '…………………………',
          user: '杨昌迪'
        },{
          datasource_name: '数据库aaa',
          url: '…………………………',
          user: '杨昌迪'
        }]
      }
    }
  }
</script>


