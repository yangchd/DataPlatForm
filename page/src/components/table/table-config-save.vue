<template>
  <div id="table-config-save">
    <!--新增界面-->
    <el-dialog :visible.sync="tableConfigDialogVisible" :center="is_center" width="70%" top="10vh"
               :close-on-click-modal="false">
      <el-form ref="tableConfigForm" :model="tableConfigForm" label-width="80px" :rules="tableConfigForm">
        <el-tabs v-model="tableConfigForm.tableConfigTabs" @tab-click="">
          <el-tab-pane label="常规配置" name="first">
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
                    <el-select v-model="jointables.dengyu" placeholder="右表列" size="large" :disabled="addLock"
                               filterable>
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
          </el-tab-pane>
          <el-tab-pane label="高级配置" name="second">
            <!--高级选项-->
            <el-row>
              <el-col :span="24" v-if="AddSetting">
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="同步选项">
                      <el-checkbox-group v-model="tableConfigForm.synvalue">
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
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelTableConfigBtn">取消</el-button>
        <el-button type="primary" @click="saveTableConfigBtn" :loading="saveFormLoading">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {getColumnList, getDataSourceList, getTableNameList, saveTableConfig} from "../../api/api";
  export default {
    data() {
      return {
        is_center: true,
        tableConfigDialogVisible: false,
        saveFormLoading: false,
        tableConfigForm: {},
        /**列配置**/
        columnRelationFlag: false,
        /**高级设置**/
        AddSetting: true,
        /**锁定部分配置**/
        addLock: false,
        editLock: false,
        /**数据源**/
        datasource: [],
        /**默认选项**/
        defaulttypeSelect: [{name: '无默认值', value: '0'}, {name: '列值为空时默认', value: '1'}, {name: '强制默认', value: '2'}],
        /**新增初始化值**/
        initTableConfigDialog: {
          id: '',
          name: '',
          description: '',
          datato: '',
          tableto: '',
          datafrom: '',
          tablefrom: '',
          tablefroms: '',
          jointable: [],
          //列配置
          relation: [],
          tablenameto: [],
          tablenamefrom: [],
          columnto: [],
          columnfrom: [],
          //高级选项
          synvalue: ['update', 'insert', 'delete'],
          syntype: '',
          logflag: false,
          errorflag: false,
          whereto: '',
          wherefrom: '',
          tableConfigTabs: 'first',
        },
      }
    },
    methods: {
      /**
       * 新增按钮
       */
      TableConfigDialog: function (val) {
        this.tableConfigDialogVisible = true;
        if (!("undefined" === typeof(val))) {
          this.columnRelationFlag = true;
          this.addLock = true;
          this.editLock = true;
          this.tableConfigForm = val;
          this.tableConfigForm.tableConfigTabs = 'first';
        } else {
          this.columnRelationFlag = false;
          this.addLock = false;
          this.editLock = false;
          this.tableConfigForm = this.initTableConfigDialog;
        }
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
          type: 'to',
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
          type: 'from',
        };
        this.tableConfigForm.tablefroms=name.toString();
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
       * 新增部分取消按钮
       */
      cancelTableConfigBtn: function () {
        this.tableConfigDialogVisible = false;
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
              this.arrayToJson(para);
              saveTableConfig(para).then((res) => {
                this.saveFormLoading = false;
                if (res.code === '0') {
                  this.$message.success(res.msg);
                  this.tableConfigDialogVisible = false;
                  this.$emit('reloadTable');
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
      /**
       * 保存数据时，将数组格式转换为json格式
       * 还有true、false等转换
       */
      arrayToJson: function (para) {
        para.jointable = JSON.stringify(para.jointable);
        para.relation = JSON.stringify(para.relation);
        para.tablenameto = JSON.stringify(para.tablenameto);
        para.tablenamefrom = JSON.stringify(para.tablenamefrom);
        para.columnto = JSON.stringify(para.columnto);
        para.columnfrom = JSON.stringify(para.columnfrom);
        para.synvalue = JSON.stringify(para.synvalue);
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

