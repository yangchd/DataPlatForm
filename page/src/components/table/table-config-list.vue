<template>
  <div id="table-config-list">
    <el-col :span="24">
      <el-form :inline="true" :model="tableTopForm" class="demo-form-inline" size="medium">
        <el-form-item>
          <el-input v-model="tableTopForm.inputKey" placeholder="同步模糊搜索"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getTableConfigListBtn">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="TableConfigAddBtn">新增</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <!--中部列表-->
    <el-table :data="table" v-loading="tableLoading" border stripe size="small"
              style="width: 100%;">
      <el-table-column prop="id" v-if="false">
      </el-table-column>
      <el-table-column prop="name" label="任务名称" width="180">
      </el-table-column>
      <el-table-column prop="lasttime" label="上次同步时间" width="180">
      </el-table-column>
      <el-table-column prop="timecost" label="上次同步耗时" width="100">
      </el-table-column>
      <el-table-column prop="synflag" label="同步开关" width="100">
      </el-table-column>
      <el-table-column label="操作" width="300px">
        <template slot-scope="scope">
          <el-button size="mini" @click="testTableConfigBtn(scope.$index, scope.row)">测试</el-button>
          <el-button size="mini" @click="editTableConfigBtn(scope.$index, scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="deleteTableConfigBtn(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="synmsg" label="同步信息">
      </el-table-column>
    </el-table>

    <!--新增界面-->
    <el-dialog title="新增同步配置" :visible.sync="addTableConfigVisible" :center="is_center" width="80%"
               :close-on-click-modal="false">
      <el-form ref="tableConfigForm" :model="tableConfigForm" label-width="80px" :rules="tableConfigForm">
        <!--上部基本-->
        <el-row>
          <el-col :span="24" v-if="false">
            <el-form-item label="id">
              <el-input v-model="tableConfigForm.id" placeholder="同步配置id"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="名称">
              <el-input v-model="tableConfigForm.name" placeholder="同步任务名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="描述">
              <el-input v-model="tableConfigForm.description" placeholder="同步任务描述"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!--中部数据源和表选择-->
        <el-row>
          <el-col :span="8">
            <el-form-item label="目标库">
              <el-select v-model="tableConfigForm.datato" placeholder="同步任务目标数据库" size="large" :disabled="addLock"
                         @change="dataSourceChange(tableConfigForm.datato,'datato')" filterable clearable>
                <el-option
                  v-for="item in datasource"
                  :label="item.name" :key="item.id" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="目标表">
              <el-select v-model="tableConfigForm.tableto" placeholder="同步任务目标表" size="large" :disabled="addLock"
                         @change="tableToChange"
                         filterable clearable>
                <el-option
                  v-for="item in tableConfigForm.tablenameto"
                  :label="item.table_name" :key="item.table_name" :value="item.table_name">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8"></el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="来源库">
              <el-select v-model="tableConfigForm.datafrom" placeholder="同步任务来源数据库" size="large" :disabled="addLock"
                         @change="dataSourceChange(tableConfigForm.datafrom,'datafrom')" filterable clearable>
                <el-option
                  v-for="item in datasource"
                  :label="item.name" :key="item.id" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="来源表">
              <el-select v-model="tableConfigForm.tablefrom" placeholder="同步任务来源表" size="large" :disabled="addLock"
                         @change="tableFromChange"
                         filterable clearable>
                <el-option
                  v-for="item in tableConfigForm.tablenamefrom"
                  :label="item.table_name" :key="item.table_name" :value="item.table_name">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8" style="text-align: center">
            <el-button type="primary" @click="tableConfigJoinBtn" :disabled="addLock">left join</el-button>
            <el-button type="primary" @click="tableConfigResetBtn" :disabled="editLock">重置</el-button>
            <el-button type="primary" @click="tableConfigConfirmBtn" :disabled="editLock">确认</el-button>
            <el-button type="primary" @click="tableConfigSettingBtn">高级</el-button>
          </el-col>
        </el-row>

        <!--左连接部分代码-->
        <el-row>
          <el-col :span="24" v-for="jointables in tableConfigForm.jointable" :key="jointables.name">
            <el-col :span="6">
              <el-form-item label="left join">
                <el-select v-model="jointables.name" placeholder="连接表" size="large" :disabled="addLock" filterable
                           @change="tableFromChange">
                  <el-option
                    v-for="item in tableConfigForm.tablenamefrom"
                    :label="item.table_name" :key="item.table_name" :value="item.table_name">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="on">
                <el-select v-model="jointables.on" placeholder="左表列" size="large" :disabled="addLock" filterable>
                  <el-option
                    v-for="item in tableConfigForm.columnfrom"
                    :label="item.column_name" :key="item.column_name" :value="item.column_name">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="=">
                <el-select v-model="jointables.dengyu" placeholder="右表列" size="large" :disabled="addLock" filterable>
                  <el-option
                    v-for="item in tableConfigForm.columnfrom"
                    :label="item.column_name" :key="item.column_name" :value="item.column_name">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6"></el-col>
          </el-col>
        </el-row>

        <!--高级选项-->
        <el-row>
          <el-col :span="24" v-if="AddSetting">
            <el-row>
              <el-col :span="12">
                <el-form-item label="同步选项">
                  <el-checkbox-group v-model="tableConfigForm.syntype">
                    <el-checkbox label="update"></el-checkbox>
                    <el-checkbox label="insert"></el-checkbox>
                    <el-checkbox label="delete"></el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
              <el-col :span="12"></el-col>
            </el-row>
            <el-row>
              <el-col :span="6">
                <el-form-item label="开启日志">
                  <el-switch
                    v-model="tableConfigForm.logflag"
                    active-color="#13ce66"
                    inactive-color="#ff4949">
                  </el-switch>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="出错继续">
                  <el-switch
                    v-model="tableConfigForm.errorflag"
                    active-color="#13ce66"
                    inactive-color="#ff4949">
                  </el-switch>
                </el-form-item>
              </el-col>
              <el-col :span="12"></el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="where">
                  <el-input v-model="tableConfigForm.whereto" placeholder="目标表where值限定"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="where">
                  <el-input v-model="tableConfigForm.wherefrom" placeholder="来源表where值限定"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row style="text-align: center">
              <el-button @click="backLeftJoinBtn">完成高级设置</el-button>
            </el-row>
          </el-col>
        </el-row>

        <!--列配置-->
        <el-row v-if="columnRelationFlag" v-for="relations in tableConfigForm.relation" :key="relations.column">
          <el-col :span="6">
            <el-form-item label="列">
              <el-input v-model="relations.columnto" disabled :value="relations.columnto"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="匹配">
              <el-select v-model="relations.columnfrom" placeholder="选择需要同步的列" size="large"
                         @change="" filterable clearable>
                <el-option
                  v-for="item in tableConfigForm.columnfrom"
                  :label="item.column_name" :key="item.column_name" :value="item.column_name">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="默认选项">
              <el-select v-model="relations.defaulttype" placeholder="选择需要同步的列" size="large"
                         @change="" filterable>
                <el-option
                  v-for="item in defaulttypeSelect"
                  :label="item.name" :key="item.value" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="默认值">
              <el-input v-model="relations.defaultvalue" placeholder="请输入默认值"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelTableConfigBtn">取消</el-button>
        <el-button type="primary" @click="saveTableConfigBtn" :loading="saveFormLoading">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    deleteTableConfig,
    getColumnList,
    getDataSourceList,
    getTableList,
    getTableNameList,
    saveTableConfig
  } from "../../api/api";
  export default {
    data() {
      return {
        /**上部菜单栏*/
        tableTopForm: {
          inputKey: '',
        },

        /**列表相关*/
        table: [],
        tableLoading: false,

        /**新增相关*/
        is_center: true,
        addTableConfigVisible: false,
        saveFormLoading: false,
        tableConfigForm: {},
        //列配置区
        columnRelationFlag: false,
        //高级设置区
        AddSetting: false,
        //确认后锁定部分区域
        addLock: false,
        editLock: false,

        /**数据源**/
        datasource: [],
        /**默认选项**/
        defaulttypeSelect: [{name: '无默认值', value: '0'}, {name: '列值为空时默认', value: '1'}, {name: '强制默认', value: '2'}],
      }
    },
    methods: {
      /**
       * 获取同步列表函数
       */
      getTableConfigList: function () {
        let para = {
          inputKey: this.inputKey,
        };
        this.tableLoading = true;
        getTableList(para).then((res) => {
          for (let i = 0; i < res.data.length; i++) {
            //对格式进行反解析
            res.data[i].jointable = JSON.parse(res.data[i].jointable);
            res.data[i].relation = JSON.parse(res.data[i].relation);
            res.data[i].columnto = JSON.parse(res.data[i].columnto);
            res.data[i].columnfrom = JSON.parse(res.data[i].columnfrom);
            res.data[i].syntype = JSON.parse(res.data[i].syntype);
            if ("true" === res.data[i].logflag) {
              res.data[i].logflag = true;
            }
            if ("true" === res.data[i].errorflag) {
              res.data[i].errorflag = true;
            }
          }
          this.table = res.data;
          this.tableLoading = false;
        }).catch((res) => {
          this.tableLoading = false;
          this.$message.error(res);
        });
      },
      /**
       * 搜索按钮
       */
      getTableConfigListBtn: function () {
        this.getTableConfigList();
      },
      /**
       * 新增按钮
       */
      TableConfigAddBtn: function () {
        this.addTableConfigVisible = true;
        this.columnRelationFlag = false;
        this.AddSetting = false;
        this.addLock = false;
        this.editLock = false;
        this.tableConfigForm = {
          id: '',
          name: '同步任务',
          description: '测试同步任务',
          datato: '',
          tableto: '',
          datafrom: '',
          tablefrom: '',
          jointable: [],
          //列配置
          relation: [],
          tablenameto: [],
          tablenamefrom: [],
          columnto: [],
          columnfrom: [],
          //高级选项
          syntype: ['update', 'insert', 'delete'],
          logflag: false,
          errorflag: false,
          whereto: '',
          wherefrom: '',
        };
      },
      /**
       * 数据源切换时执行
       */
      dataSourceChange: function (id, type) {
        let para = {
          id: id,
        };
        getTableNameList(para).then((res) => {
          if (res.code === "0") {
            if (type === 'datato') {
              this.tableConfigForm.tablenameto = res.data;
            } else if (type === 'datafrom') {
              this.tableConfigForm.tablenamefrom = res.data;
            }
          } else {
            this.$message.error(res.msg);
          }
        }).catch((res) => {
          this.$message.error(res);
        });
      },
      /**
       * 表名称变换
       */
      tableToChange: function () {
        let para = {
          id: this.tableConfigForm.datato,
          name: this.tableConfigForm.tableto,
        };
        getColumnList(para).then((res) => {
          if (res.code === "0") {
            this.tableConfigForm.columnto = res.data;
          } else {
            this.$message.error(res.msg);
          }
        }).catch((res) => {
          this.$message.error(res);
        });
      },
      tableFromChange: function () {
        //先做所有列值的获取
        let name = [];
        name.push(this.tableConfigForm.tablefrom);
        for (let i = 0; i < this.tableConfigForm.jointable.length; i++) {
          if ('' !== this.tableConfigForm.jointable[i].name && null !== this.tableConfigForm.jointable[i].name) {
            let flag = false;
            for (let m = 0; m < name.length; m++) {
              if (name[m] === this.tableConfigForm.jointable[i].name) {
                flag = true;
              }
            }
            if (!flag) {
              name.push(this.tableConfigForm.jointable[i].name);
            }
          }
        }
        let para = {
          id: this.tableConfigForm.datafrom,
          name: name.toString(),
        };
        getColumnList(para).then((res) => {
          if (res.code === "0") {
            this.tableConfigForm.columnfrom = res.data;
          } else {
            this.$message.error(res.msg);
          }
        }).catch((res) => {
          this.$message.error(res);
        });
      },
      /**
       * 左连接按钮
       */
      tableConfigJoinBtn: function () {
        //进行一下非空校验
        if ('' === this.tableConfigForm.tablefrom || null === this.tableConfigForm.tablefrom
          || '' === this.tableConfigForm.datafrom || null === this.tableConfigForm.datafrom) {
          this.$message.error('未选择数据库或表时不能添加左连接');
          return;
        }
        //新增校验
        let jointable = {name: '', on: '', dengyu: ''};
        for (let y = 0; y < this.tableConfigForm.jointable.length; y++) {
          if (this.tableConfigForm.jointable[y].name === jointable.name
            || this.tableConfigForm.jointable[y].on === jointable.on
            || this.tableConfigForm.jointable[y].dengyu === jointable.dengyu) {
            this.$message.error('有左连接选项未全部确认时，不能添加新的左连接');
            return;
          }
        }
        this.tableConfigForm.jointable.push(jointable);
      },
      /**
       * 配置重置按钮
       */
      tableConfigResetBtn: function () {
        this.$confirm('确认要重置左连接及列配置吗？', '提示', {}).then(() => {
          this.tableConfigForm.relation = [];
          this.tableConfigForm.jointable = [];
          this.columnRelationFlag = false;
          this.addLock = false;
        }).catch(() => {
          //取消操作
        });
      },
      /**
       * 点击确认按钮，加载列配置
       */
      tableConfigConfirmBtn: function () {
        if (this.addLock) {
          return;
        }
        this.addLock = true;
        this.tableConfigForm.relation = [];
        for (let i = 0; i < this.tableConfigForm.columnto.length; i++) {
          let relation = {
            columnto: this.tableConfigForm.columnto[i].column_name,
            columnfrom: '',
            defaulttype: '0',
            defaultvalue: '',
          };
          this.tableConfigForm.relation.push(relation);
        }
        this.columnRelationFlag = true;
      },
      /**
       * 高级设置按钮
       */
      tableConfigSettingBtn: function () {
        this.$confirm('高级设置请小心操作？', '提示', {
          confirmButtonText: '开启',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.AddSetting = true;
          this.columnRelationFlag = false;
        }).catch(() => {
          //取消操作
        });
      },
      /**
       * 高级设置返回按钮
       */
      backLeftJoinBtn: function () {
        this.$confirm('返回修改其他选项？', '提示', {}).then(() => {
          this.AddSetting = false;
          this.columnRelationFlag = true;
        }).catch(() => {
          //取消操作
        });
      },
      /**
       * 新增部分取消按钮
       */
      cancelTableConfigBtn: function () {
        this.addTableConfigVisible = false;
      },
      /**
       * 新增部分保存按钮
       */
      saveTableConfigBtn: function () {
        this.$refs.tableConfigForm.validate((valid) => {
          if (valid) {
            this.$confirm('确认提交吗？', '提示', {}).then(() => {
              this.saveFormLoading = true;
              let para = Object.assign({}, this.tableConfigForm);
              para.jointable = JSON.stringify(para.jointable);
              para.relation = JSON.stringify(para.relation);
              para.tablenameto = JSON.stringify(para.tablenameto);
              para.tablenamefrom = JSON.stringify(para.tablenamefrom);
              para.columnto = JSON.stringify(para.columnto);
              para.columnfrom = JSON.stringify(para.columnfrom);
              para.syntype = JSON.stringify(para.syntype);
              saveTableConfig(para).then((res) => {
                this.saveFormLoading = false;
                if (res.code === '0') {
                  this.$message.success(res.msg);
                  this.getTableConfigList();
                  this.addTableConfigVisible = false;
                } else {
                  this.$message.error(res.msg);
                }
              }).catch(() => {
                this.saveFormLoading = false;
              });
            }).catch(() => {
              //取消操作
            });
          }
        });
      },
      /**table列表中，测试、编辑、删除**/
      testTableConfigBtn: function (index, row) {

      },
      editTableConfigBtn: function (index, row) {
        this.addTableConfigVisible = true;
        this.columnRelationFlag = true;
        this.AddSetting = false;
        this.addLock = true;
        this.editLock = true;
        this.tableConfigForm = row;
      },
      deleteTableConfigBtn: function (index, row) {
        this.$confirm('确认删除吗？', '提示', {}).then(() => {
          let para = Object.assign({}, row);
          deleteTableConfig(para).then((res) => {
            if (res.code === '0') {
              this.$message.success(res.msg);
              this.getTableConfigList();
            } else {
              this.$message.error(res.msg);
            }
          });
        }).catch(() => {
          //取消操作
        });
      },
      /**
       * 初始化函数
       */
      datasourceInit: function () {
        let para = {};
        getDataSourceList(para).then((res) => {
          this.datasource = res.data;
        }).catch((res) => {
          this.$message.error(res);
        });
      }
    },
    /**页面加载以后执行**/
    mounted(){
      this.datasourceInit();
    },
  }
</script>

<style>

</style>

