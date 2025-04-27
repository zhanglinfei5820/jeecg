import request from '../../request/index.js';
//根据id获取商品详情
export async function getProductDetail(id) {
	const res = await request({
		url: "/functionMenu/productinfo/" + id,
		method: "get",
	})
	return res
}
//根据产品level获取多个产品信息
export async function getMoreProductDetail() {
	const res = await request({
		url: "/functionMenu/productinfo/list",
		method: "get",
		data: {
			level:2
		}
	})
	return res
}
