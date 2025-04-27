<route lang="json5" type="page">
  {
    layout: 'default',
    style: {
      navigationBarTitleText: '车辆管理' 
    },
  }
</route>
<template>
	<view class="container">
		<!-- 搜索区域 -->
		<view class="search-bar">
			<view class="search-input-box">
				<input type="text" v-model="keyword" placeholder="车牌号/司机姓名/广告名称" placeholder-class="placeholder-style" class="search-input" confirm-type="search" @confirm="onSearch" />
				<text class="clear-icon" v-if="keyword" @click="onClear">×</text>
			</view>
			<view class="search-btn" @click="onSearch">搜索</view>
		</view>

		<!-- 筛选区域 -->
		<view class="filter-bar">
			<view class="filter-item" @click="toggleSort('vehicleType')">
				<text>车辆类型</text>
			</view>
			<view 
				class="filter-item" 
				:class="{'active-sort': sortBy === 'installationTime'}" 
				@click="toggleSort('installationTime')"
			>
				<text>安装时间排序</text>
				<view class="sort-arrow-wrapper">
					<view class="sort-arrow sort-arrow-up" :class="{'active': sortBy === 'installationTime' && sortOrder === 'asc'}"></view>
					<view class="sort-arrow sort-arrow-down" :class="{'active': sortBy === 'installationTime' && sortOrder === 'desc'}"></view>
				</view>
			</view>
		</view>

		<!-- 车辆列表 -->
		<z-paging 
			ref="paging" 
			v-model="vehicleList" 
			@query="queryList" 
			:auto-show-back-to-top="true" 
			:auto-clean-list-when-reload="true"
			:fixed="false"
			:use-page-scroll="true"
			:refresher-enabled="true"
			:refresher-threshold="80"
			:refresher-default-style="'black'"
		>
			<view class="vehicle-list">
				<view class="vehicle-item" v-for="(item, index) in vehicleList" :key="index">
					<view class="item-header">
						<view class="header-left">
							<image src="/static/images/icon-car-selected.png" class="car-logo"></image>
							<text class="plate-number">{{ item.plateNumber }}</text>
						</view>
						<view class="header-right">
							<text class="maintenance-text">维护金发放</text>
							<text class="maintenance-count">{{ item.maintenanceCount }}</text>
							<text class="maintenance-text">次</text>
						</view>
					</view>
					<view class="item-content">
						<view class="content-left">
							<image :src="item.installationImage" mode="aspectFill" class="vehicle-image"></image>
						</view>
						<view class="content-right">
							<view class="info-row"><text class="info-label">车辆类型</text><text class="info-value text-ellipsis">{{ item.vehicleType }}</text></view>
							<view class="info-row"><text class="info-label">品牌型号</text><text class="info-value text-ellipsis">{{ item.brand }}</text></view>
							<view class="info-row"><text class="info-label">所属公司</text><text class="info-value text-ellipsis">{{ item.companyName }}</text></view>
							<view class="info-row"><text class="info-label">广告名称</text><text class="info-value text-ellipsis">{{ item.adName }}</text></view>
							<view class="info-row"><text class="info-label">安装时间</text><text class="info-value text-ellipsis">{{ item.installationTime }}</text></view>
						</view>
					</view>
				</view>
			</view>
		</z-paging>
	</view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { http } from '@/utils/http'
import { useToast } from 'wot-design-uni'
import { getFileAccessHttpUrl } from '@/common/uitls'

interface Vehicle {
	plateNumber: string
	maintenanceCount: number
	imageUrl: string
	vehicleType: string
	brand: string
	companyName: string
	adName: string
	installationImage: string
	installationTime: string
}

interface VehicleListResponse {
	records: Vehicle[]
	total: number
	size: number
	current: number
	pages: number
}

const toast = useToast()
const paging = ref(null)
const vehicleList = ref<Vehicle[]>([])
const keyword = ref('')
const sortBy = ref('installationTime')
const sortOrder = ref('desc')

// 查询列表数据
const queryList = async (pageNo: number, pageSize: number) => {
	try {
		const params = {
			status: 1,
			pageNo,
			pageSize,
			keyword: keyword.value,
			column: sortBy.value,
			order: sortOrder.value
		}
		
		const res = await http.get<VehicleListResponse>('/ad/adVehicle/list', params)
		console.log('res', res)
		if (res.code === 200) {
			// Add file access URL prefix to image URLs
			const records = res.result.records.map(item => ({
				...item,
				installationImage: getFileAccessHttpUrl(item.installationImage)
			}))
			console.log('records', records)
			paging.value.complete(records)
		} else {
			toast.error(res.msg || '获取数据失败')
			paging.value.complete(false)
		}
	} catch (error) {
		console.error('获取车辆列表失败:', error)
		toast.error('获取数据失败')
		paging.value.complete(false)
	}
}

// 搜索
const onSearch = () => {
	paging.value.reload()
}

// 清空搜索
const onClear = () => {
	keyword.value = ''
	paging.value.reload()
}

// 切换排序
const toggleSort = (field: string) => {
	if (sortBy.value === field) {
		sortOrder.value = sortOrder.value === 'desc' ? 'asc' : 'desc'
	} else {
		sortBy.value = field
		sortOrder.value = 'desc'
	}
	paging.value.reload()
}
</script>

<style lang="scss" scoped>
.container {
	padding: 0 15px 15px;
	background-color: #f8f8f8;
	min-height: 100vh;
	position: relative;
	z-index: 1;
}

.search-bar {
	padding: 10px 15px;
	background-color: #ffffff;
	margin: 0 -15px 10px;
	display: flex;
	align-items: center;
	position: sticky;
	top: 0;
	z-index: 2;
}

.search-input-box {
	flex: 1;
	position: relative;
	height: 36px;
	border: 1px solid #e0e0e0;
	border-radius: 4px;
	padding: 0 10px;
	margin-right: 10px;
	display: flex;
	align-items: center;
	
	.search-input {
		flex: 1;
		height: 100%;
		font-size: 14px;
	}
	
	.placeholder-style {
		color: #999;
		font-size: 14px;
	}
	
	.clear-icon {
		font-size: 18px;
		color: #999;
		width: 20px;
		height: 20px;
		text-align: center;
		line-height: 20px;
	}
}

.search-btn {
	padding: 0 15px;
	height: 36px;
	line-height: 36px;
	background-color: #2979ff;
	color: #ffffff;
	font-size: 14px;
	border-radius: 4px;
}

.filter-bar {
	display: flex;
	justify-content: space-between;
	padding: 10px 5px; // 筛选条内边距
	margin-bottom: 10px;
	font-size: 14px;
	color: #606266;

	.filter-item {
		display: flex;
		align-items: center;
		
		.car-icon {
			width: 16px;
			height: 16px;
			margin-right: 4px;
		}
		
		.sort-arrow-wrapper {
			display: flex;
			flex-direction: column;
			margin-left: 4px;
			height: 14px;
			width: 10px;
			
			.sort-arrow {
				width: 0;
				height: 0;
				border-left: 4px solid transparent;
				border-right: 4px solid transparent;
				
				&.sort-arrow-up {
					border-bottom: 4px solid #c0c4cc;
					margin-bottom: 2px;
					
					&.active {
						border-bottom-color: #ff9900;
					}
				}
				
				&.sort-arrow-down {
					border-top: 4px solid #c0c4cc;
					
					&.active {
						border-top-color: #ff9900;
					}
				}
			}
		}
	}
	
	.active-sort {
		color: #ff9900; // 使用橙色高亮
	}
}

.vehicle-list {
	.vehicle-item {
		background-color: #ffffff;
		border-radius: 8px;
		margin-bottom: 10px;
		padding: 15px;
		box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05); // 添加轻微阴影

		.item-header {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 10px;

			.header-left {
				display: flex;
				align-items: center;
				.car-logo {
					width: 20px; // 设置 logo 图片大小
					height: 20px;
					margin-right: 8px; // 调整 logo 和车牌号间距
				}
				.plate-number {
					font-size: 16px;
					font-weight: bold;
				}
			}
			.header-right {
				font-size: 12px;
				.maintenance-text {
					color: #6E6E6E;
				}
				.maintenance-count {
					color: #ff9900; // 橙色数字
					margin: 0 2px; // 给数字左右一点间距
				}
			}
		}

		.item-content {
			display: flex;
			align-items: flex-start; // 垂直顶部对齐

			.content-left {
				margin-right: 15px;
				.vehicle-image {
					width: 100px; // 明确设置图片宽度
					height: 75px; // 明确设置图片高度
					border-radius: 4px; // 图片圆角
				}
			}

			.content-right {
				flex: 1;
				font-size: 13px;
				color: #606266;

				.info-row {
					display: flex;
					margin-bottom: 5px;
					.info-label {
						color: #909399;
						width: 70px; // 固定标签宽度
						flex-shrink: 0; // 防止标签被压缩
					}
					.info-value {
						flex: 1;
						word-break: break-all; // 值过长时换行
					}
					.text-ellipsis {
						white-space: nowrap;
						overflow: hidden;
						text-overflow: ellipsis;
						max-width: 180px; // 设置最大宽度，根据实际情况调整
					}
				}
			}
		}
	}
}

// 移除旧的 .charts 样式
// .charts{ ... }
</style>