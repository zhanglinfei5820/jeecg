<route lang="json5" type="page">
  {
    style: {
      navigationStyle: 'custom',
      navigationBarTitleText: '广告维护'
    }
  }
</route>

<template>
  <view class="container">
    <!-- 顶部状态栏占位 -->
    <view class="status-bar" :style="{ height: statusBarHeight + 'px' }"></view>
    
    <!-- 导航栏 -->
    <view class="nav-bar">
      <view class="nav-left" @click="goBack">
        <text class="iconfont icon-left"></text>
      </view>
      <view class="nav-title">广告维护</view>
      <view class="nav-right">
        <text class="iconfont icon-more"></text>
      </view>
    </view>

    <!-- Tab切换 -->
    <view class="tab-bar">
      <view 
        v-for="(tab, index) in tabs" 
        :key="index"
        class="tab-item"
        :class="{ active: currentTab === index }"
        @click="switchTab(index)"
      >
        {{ tab }}
      </view>
    </view>

    <!-- 搜索区域 -->
    <view class="search-bar">
      <view class="search-input-box">
        <input 
          type="text" 
          v-model="keyword" 
          :placeholder="searchPlaceholder" 
          placeholder-class="placeholder-style" 
          class="search-input" 
          confirm-type="search" 
          @confirm="onSearch" 
        />
        <text class="clear-icon" v-if="keyword" @click="onClear">×</text>
      </view>
      <view class="search-btn" @click="onSearch">搜索</view>
    </view>

    <!-- 筛选区域 -->
    <view class="filter-bar">
      <view class="filter-item" @click="toggleFilter('all')">
        <text>全部</text>
        <text class="filter-arrow">▼</text>
      </view>
      <view class="filter-item" @click="toggleSort">
        <text>{{ currentSortText }}</text>
        <view class="sort-arrow-wrapper">
          <view class="sort-arrow sort-arrow-up" :class="{'active': sortOrder === 'asc'}"></view>
          <view class="sort-arrow sort-arrow-down" :class="{'active': sortOrder === 'desc'}"></view>
        </view>
      </view>
    </view>

    <!-- 列表区域 -->
    <z-paging 
      ref="paging" 
      v-model="adList" 
      @query="queryList" 
      :auto-show-back-to-top="true" 
      :auto-clean-list-when-reload="true"
      :fixed="false"
      :use-page-scroll="true"
      :refresher-enabled="true"
      :refresher-threshold="80"
      :refresher-default-style="'black'"
    >
      <view class="ad-list">
        <view class="ad-item" v-for="(item, index) in adList" :key="index">
          <view class="item-header">
            <view class="header-left">
              <image src="/static/images/icon-car-selected.png" class="car-logo"></image>
              <text class="plate-number">{{ item.plateNumber }}</text>
            </view>
            <view class="header-right">
              <text class="status-tag" :class="item.status">{{ item.statusText }}</text>
            </view>
          </view>
          <view class="item-content">
            <view class="content-left">
              <image :src="item.adImage" mode="aspectFill" class="ad-image"></image>
            </view>
            <view class="content-right">
              <view class="info-row"><text class="info-label">车辆类型</text><text class="info-value">{{ item.vehicleType }}</text></view>
              <view class="info-row"><text class="info-label">品牌型号</text><text class="info-value">{{ item.brand }}</text></view>
              <view class="info-row"><text class="info-label">所属公司</text><text class="info-value">{{ item.companyName }}</text></view>
              <view class="info-row"><text class="info-label">广告名称</text><text class="info-value">{{ item.adName }}</text></view>
              <view class="info-row">
                <text class="info-label">到期时间</text>
                <text class="info-value" :class="{'expired': item.expireTime}">
                  {{ item.expiryDate }}
                  <text v-if="item.expireTime" class="expired-text">{{ item.expiredDays }}</text>
                </text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </z-paging>
  </view>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { http } from '@/utils/http'
import { useToast } from 'wot-design-uni'
import { getFileAccessHttpUrl } from '@/common/uitls'

interface AdItem {
  plateNumber: string
  status: string
  adImage: string
  vehicleType: string
  brand: string
  companyName: string
  adName: string
  expiryDate: string
}

interface AdListResponse {
  records: AdItem[]
  total: number
  size: number
  current: number
}

const toast = useToast()
const paging = ref(null)
const statusBarHeight = uni.getSystemInfoSync().statusBarHeight
const keyword = ref('')
const currentTab = ref(0)
const sortOrder = ref('desc')

// Tab配置
const tabs = ['年检维护', '破损维护', '有效期维护']

// 搜索框提示文字
const searchPlaceholder = computed(() => '广告名称')

// 排序文字
const currentSortText = computed(() => '年检到期时间降序')

// 广告列表数据
const adList = ref([])

// 返回上一页
const goBack = () => {
  uni.navigateBack()
}

// 切换Tab
const switchTab = (index: number) => {
  currentTab.value = index
  paging.value?.reload()
}

// 切换排序
const toggleSort = () => {
  sortOrder.value = sortOrder.value === 'desc' ? 'asc' : 'desc'
  paging.value?.reload()
}

// 切换筛选
const toggleFilter = (type: string) => {
  // 实现筛选逻辑
  paging.value?.reload()
}

// 搜索
const onSearch = () => {
  paging.value?.reload()
}

// 清空搜索
const onClear = () => {
  keyword.value = ''
  paging.value?.reload()
}

// 查询列表数据
const queryList = async (pageNo: number, pageSize: number) => {
  try {
    const params = {
      pageNo,
      pageSize,
      keyword: keyword.value,
      type: currentTab.value,
      order: sortOrder.value
    }
    
    const res = await http.get<AdListResponse>('/ad/adInspection/vxList', params)
    console.log('res', res)
    if (res.code === 200) {
      const records = res.result.records.map(item => ({
        ...item,
        adImage: getFileAccessHttpUrl(item.adImage),
        statusText: getStatusText(item.status),
        expireTime: isExpired(item.expiryDate),
        expiredDays: getExpiredDays(item.expiryDate)
      }))
      paging.value.complete(records)
    } else {
      toast.error(res.msg || '获取数据失败')
      paging.value.complete(false)
    }
  } catch (error) {
    console.error('获取广告列表失败:', error)
    toast.error('获取数据失败')
    paging.value.complete(false)
  }
}

// 获取状态文字
const getStatusText = (status: string) => {
  const statusMap = {
    'pending': '待安装',
    'installed': '已安装',
    'expired': '已过期'
  }
  return statusMap[status] || status
}

// 检查是否过期
const isExpired = (date: string) => {
  return new Date(date).getTime() < Date.now()
}

// 获取过期天数
const getExpiredDays = (date: string) => {
  const days = Math.floor((Date.now() - new Date(date).getTime()) / (1000 * 60 * 60 * 24))
  return days > 0 ? `距今已过${days}天` : ''
}
</script>

<style lang="scss" scoped>
.container {
  min-height: 100vh;
  background-color: #f8f8f8;
}

.status-bar {
  background-color: #ffffff;
}

.nav-bar {
  display: flex;
  align-items: center;
  height: 44px;
  background-color: #ffffff;
  padding: 0 15px;
  
  .nav-left {
    width: 24px;
    text-align: center;
    
    .icon-left {
      font-size: 20px;
    }
  }
  
  .nav-title {
    flex: 1;
    text-align: center;
    font-size: 16px;
    font-weight: 500;
  }
  
  .nav-right {
    width: 24px;
    text-align: center;
    
    .icon-more {
      font-size: 20px;
    }
  }
}

.tab-bar {
  display: flex;
  background-color: #ffffff;
  padding: 0 15px;
  border-bottom: 1px solid #f0f0f0;
  
  .tab-item {
    flex: 1;
    height: 44px;
    line-height: 44px;
    text-align: center;
    font-size: 14px;
    color: #666666;
    position: relative;
    
    &.active {
      color: #2979ff;
      font-weight: 500;
      
      &::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%);
        width: 20px;
        height: 2px;
        background-color: #2979ff;
        border-radius: 1px;
      }
    }
  }
}

.search-bar {
  padding: 10px 15px;
  background-color: #ffffff;
  display: flex;
  align-items: center;
}

.search-input-box {
  flex: 1;
  height: 36px;
  background-color: #f5f5f5;
  border-radius: 18px;
  padding: 0 15px;
  margin-right: 10px;
  display: flex;
  align-items: center;
  
  .search-input {
    flex: 1;
    height: 100%;
    font-size: 14px;
  }
  
  .clear-icon {
    padding: 0 5px;
    color: #999999;
  }
}

.search-btn {
  width: 60px;
  height: 36px;
  line-height: 36px;
  text-align: center;
  background-color: #2979ff;
  color: #ffffff;
  font-size: 14px;
  border-radius: 18px;
}

.filter-bar {
  display: flex;
  justify-content: space-between;
  padding: 10px 15px;
  background-color: #ffffff;
  margin-bottom: 10px;
  
  .filter-item {
    display: flex;
    align-items: center;
    font-size: 14px;
    color: #666666;
    
    .filter-arrow {
      font-size: 12px;
      margin-left: 4px;
    }
    
    .sort-arrow-wrapper {
      display: flex;
      flex-direction: column;
      margin-left: 4px;
      height: 12px;
      
      .sort-arrow {
        width: 0;
        height: 0;
        border-left: 4px solid transparent;
        border-right: 4px solid transparent;
        
        &.sort-arrow-up {
          border-bottom: 4px solid #cccccc;
          margin-bottom: 2px;
          
          &.active {
            border-bottom-color: #2979ff;
          }
        }
        
        &.sort-arrow-down {
          border-top: 4px solid #cccccc;
          
          &.active {
            border-top-color: #2979ff;
          }
        }
      }
    }
  }
}

.ad-list {
  padding: 0 15px;
  
  .ad-item {
    background-color: #ffffff;
    border-radius: 8px;
    margin-bottom: 10px;
    padding: 15px;
    
    .item-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12px;
      
      .header-left {
        display: flex;
        align-items: center;
        
        .car-logo {
          width: 20px;
          height: 20px;
          margin-right: 6px;
        }
        
        .plate-number {
          font-size: 16px;
          font-weight: 500;
          color: #333333;
        }
      }
      
      .header-right {
        .status-tag {
          padding: 2px 8px;
          border-radius: 2px;
          font-size: 12px;
          
          &.pending {
            background-color: #fff7e6;
            color: #ff9900;
          }
          
          &.installed {
            background-color: #f6ffed;
            color: #52c41a;
          }
          
          &.expired {
            background-color: #fff1f0;
            color: #f5222d;
          }
        }
      }
    }
    
    .item-content {
      display: flex;
      
      .content-left {
        margin-right: 12px;
        
        .ad-image {
          width: 100px;
          height: 75px;
          border-radius: 4px;
        }
      }
      
      .content-right {
        flex: 1;
        
        .info-row {
          display: flex;
          margin-bottom: 8px;
          font-size: 13px;
          
          .info-label {
            width: 70px;
            color: #999999;
          }
          
          .info-value {
            flex: 1;
            color: #333333;
            
            &.expired {
              color: #f5222d;
              
              .expired-text {
                margin-left: 5px;
              }
            }
          }
        }
      }
    }
  }
}
</style>