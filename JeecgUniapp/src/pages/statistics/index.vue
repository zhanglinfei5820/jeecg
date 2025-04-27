<template>
  <view class="container">
    <!-- 顶部轮播图 -->
    <swiper class="banner" circular :indicator-dots="true" :autoplay="true" interval="3000" duration="500"
      :indicator-color="'rgba(0, 0, 0, 0.3)'" :indicator-active-color="'#F29600'">
      <swiper-item>
        <image src="@/static/images/img-home-ad1.png" mode="widthFix" />
      </swiper-item>
      <swiper-item>
        <image src="@/static/images/img-home-ad1.png" mode="widthFix" />
      </swiper-item>
    </swiper>

    <!-- 功能按钮区域 -->
    <view class="function-area">
      <view class="function-item" @click="switchTab('/pages/vehicle/vehicle')">
        <image src="@/static/images/icon-home-car.png" mode="widthFix" />
        <text>车辆管理</text>
      </view>
      <view class="function-item" @click="switchTab('/pages/inspection/index')">
        <image src="@/static/images/icon-home-nianjian.png" mode="widthFix" />
        <text>年检维护</text>
      </view>
      <view class="function-item" @click="switchTab('/pages/ad/index')">
        <image src="@/static/images/icon-home-ad.png" mode="widthFix" />
        <text>广告有效期</text>
      </view>
      <view class="function-item" @click="switchTab('/pages/maintenance/index')">
        <image src="@/static/images/icon-home-weihu.png" mode="widthFix" />
        <text>破损维护</text>
      </view>
    </view>

    <!-- 车辆统计 -->
    <view class="statistics-card">
      <view class="card-header">
        <text class="title">车辆统计</text>
        <view class="more-btn" @click="goToDetail('vehicle')">查看明细</view>
      </view>
      <view class="card-content">
        <view class="chart-container">
          <canvas canvas-id="vehicleChart" id="vehicleChart" class="charts" @touchend="tap"/>
        </view>
        <view class="info-container two-column-layout">
          <!-- Column 1: Type -->
          <view class="info-column">
            <text class="info-column-header">车辆类型</text>
            <view class="info-list">
              <view class="info-item" v-for="(item, index) in vehicleData" :key="'type-' + index">
                <view class="dot" :style="{ backgroundColor: chartColors[index] }"></view>
                <text class="name">{{item.name}}</text>
              </view>
            </view>
          </view>
          <!-- Column 2: Value -->
          <view class="info-column value-column">
            <text class="info-column-header">数量 (辆)</text>
            <view class="info-list">
              <view class="info-item" v-for="(item, index) in vehicleData" :key="'value-' + index">
                <text class="value">{{item.value}}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 广告统计 -->
    <view class="statistics-card">
      <view class="card-header">
        <text class="title">广告统计</text>
        <view class="more-btn" @click="goToDetail('ad')">查看明细</view>
      </view>
      <view class="card-content">
        <view class="chart-container">
          <canvas canvas-id="adChart" id="adChart" class="charts" @touchend="tap"/>
        </view>
        <view class="info-container two-column-layout">
          <!-- Column 1: Type -->
          <view class="info-column">
            <text class="info-column-header">分类</text>
            <view class="info-list">
              <view class="info-item" v-for="(item, index) in adData" :key="'adtype-' + index">
                <view class="dot" :style="{ backgroundColor: adChartColors[index] }"></view>
                <text class="name">{{item.name}}</text>
              </view>
            </view>
          </view>
          <!-- Column 2: Value -->
          <view class="info-column value-column">
            <text class="info-column-header">数量 (条)</text>
            <view class="info-list">
              <view class="info-item" v-for="(item, index) in adData" :key="'advalue-' + index">
                <text class="value">{{item.value}}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部logo -->
    <view class="footer">
      <image src="@/static/images/img-home-ad2.png" mode="widthFix" />
    </view>
  </view>
</template>

<script>
import uCharts from '../qiun-data-charts/u-charts.js';
import { http } from '@/utils/http'

var uChartsInstance = {};
export default {
  data() {
    return {
      cWidth: 750,
      cHeight: 500,
      chartColors: ["#1890FF", "#FFA042", "#9370DB"],
      vehicleTotal: 286,
      vehicleData: [
        { name: '轿车', value: 105 },
        { name: 'SUV', value: 98 },
        { name: 'MPV', value: 83 }
      ],
      adChartColors: ["#4CD964", "#FFA042", "#FF3B30"],
      adTotal: 196,
      adData: [
        { name: '年检待安装', value: 105 },
        { name: '破损待处理', value: 12 },
        { name: '即将过期', value: 83 }
      ]
    };
  },
  onReady() {
    // 获取系统信息以计算图表尺寸
    const systemInfo = uni.getSystemInfoSync();
    // 将宽度设置为屏幕宽度的一半减去边距
    this.cWidth = systemInfo.windowWidth * 0.5;
    this.cHeight = this.cWidth; // Maintain aspect ratio or adjust as needed
    this.initCharts();
  },
  methods: {
    async initCharts() {
      await this.getServerData();
      this.drawVehicleChart();
      this.drawAdChart();
    },
    async getServerData() {
      try {
        const res = await http.get('/ad/adVehicle/statistics', {});
        if (res.success && res.result) {
          // 处理服务器返回的数据
          const data = res.result;
          // 更新数据...
        }
      } catch (error) {
        console.error('获取统计数据失败:', error);
      }
    },
    drawVehicleChart() {
      const ctx = uni.createCanvasContext('vehicleChart', this);
      uChartsInstance['vehicleChart'] = new uCharts({
        type: "ring",
        context: ctx,
        width: this.cWidth,
        height: this.cHeight,
        series: [{
          data: this.vehicleData
        }],
        animation: true,
        background: "#FFFFFF",
        color: this.chartColors,
        padding: [0, 0, 0, 0],
        dataLabel: false,
        title: {
          name: this.vehicleTotal.toString(),
          fontSize: 25,
          color: "#333333"
        },
        subtitle: {
          name: "车辆总数",
          fontSize: 14,
          color: "#666666"
        },
        extra: {
          ring: {
            ringWidth: 15,
            activeOpacity: 0.5,
            activeRadius: 10,
            offsetAngle: 0,
            labelWidth: 0,
            border: false,
            borderWidth: 3,
            borderColor: "#FFFFFF",
            linearType: null,
            showLabel: false
          }
        }
      });
    },
    drawAdChart() {
      const ctx = uni.createCanvasContext('adChart', this);
      uChartsInstance['adChart'] = new uCharts({
        type: "ring",
        context: ctx,
        width: this.cWidth,
        height: this.cHeight,
        series: [{
          data: this.adData
        }],
        animation: true,
        background: "#FFFFFF",
        color: this.adChartColors,
        padding: [0, 0, 0, 0],
        dataLabel: false,
        title: {
          name: this.adTotal.toString(),
          fontSize: 25,
          color: "#333333"
        },
        subtitle: {
          name: "广告总数",
          fontSize: 14,
          color: "#666666"
        },
        extra: {
          ring: {
            ringWidth: 15,
            activeOpacity: 0.5,
            activeRadius: 10,
            offsetAngle: 0,
            labelWidth: 0,
            border: false,
            borderWidth: 3,
            borderColor: "#FFFFFF",
            linearType: null,
            showLabel: false
          }
        }
      });
    },
    tap(e) {
      uChartsInstance[e.target.id].touchLegend(e);
      uChartsInstance[e.target.id].showToolTip(e);
    },
    navigateTo(url) {
      uni.navigateTo({ url });
    },
    switchTab(url) {
      uni.switchTab({ url });
    },
    goToDetail(type) {
      uni.navigateTo({
        url: `/pages/${type}/detail`
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
  padding-bottom: 100rpx;
}

.banner {
  width: 100%;
  height: 300rpx;
  border-radius: 12rpx;
  overflow: hidden;
  margin-bottom: 20rpx;
  
  swiper-item {
    width: 100%;
    height: 100%;
    
    image {
      width: 100%;
      height: 100%;
    }
  }
}

.function-area {
  display: flex;
  justify-content: space-between;
  padding: 20rpx;
  background-color: #fff;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
}

.function-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  
  image {
    width: 80rpx;
    height: 80rpx;
    margin-bottom: 10rpx;
  }
  
  text {
    font-size: 24rpx;
    color: #333;
  }
}

.statistics-card {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
  
  .title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    position: relative;
    padding-left: 20rpx;
    display: flex;
    align-items: center;
    
    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      width: 6rpx;
      height: 30rpx;
      background-color: #F29600;
    }
    
    &::after {
      content: '';
      display: inline-block;
      width: 10rpx;
      height: 100%;
      background-color: #F29600;
      margin-right: 10rpx;
      border-radius: 2rpx;
    }
  }
  
  .more-btn {
    font-size: 26rpx;
    color: #fff;
    background-color: #0E5398;
    padding: 10rpx 30rpx;
    border-radius: 30rpx;
  }
}

.card-content {
  display: flex;
  align-items: flex-start;
  padding: 0;
  margin: 0 -20rpx;
}

.chart-container {
  width: 50%;
  padding: 0;
  margin: 0;
}

.charts {
  width: 100%;
  height: 400rpx;
}

.info-container.two-column-layout {
  display: flex;
  width: 50%;
  padding-left: 10rpx;
  box-sizing: border-box;
}

.info-column {
  display: flex;
  flex-direction: column;
}

.info-column:first-child {
  margin-right: 20rpx;
}

.info-column.value-column {
  align-items: flex-start;
}

.info-column-header {
  font-size: 28rpx;
  color: #666;
  font-weight: bold;
  margin-bottom: 20rpx;
  text-align: left;
  white-space: nowrap;
}

.info-list {

}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
  min-height: 28rpx;
  line-height: 28rpx;
}

.info-column:not(.value-column) .info-item {
  /* Dot and name */
}

.info-column:not(.value-column) .dot {
  width: 20rpx;
  height: 20rpx;
  border-radius: 50%;
  margin-right: 10rpx;
}

.info-column:not(.value-column) .name {
  font-size: 28rpx;
  color: #333;
  white-space: nowrap;
}

.info-column.value-column .info-item {
   justify-content: flex-start;
   box-sizing: border-box;
}

.info-column.value-column .value {
  font-size: 28rpx;
  color: #333;
  font-weight: bold;
  text-align: left;
}

.footer {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 40rpx;
  width: 100%;
  
  image {
    width: 100%;
    height: 120rpx;
  }
}
</style>