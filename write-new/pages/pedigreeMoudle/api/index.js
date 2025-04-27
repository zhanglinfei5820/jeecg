import request from '../../../request/index.js';
//根据uid创建族谱
export async function createPedigree(parms) {
	const res = await request({
					url: "/functionMenu/genealogy/add",
					method: "post",
					data: parms
				})
	return res
}

//获取族谱信息
export async function getPedigreeInfo(id) {
	const res = await request({
					url: "/functionMenu/genealogy/" + id,
					method: "get"
				})
	return res
}


//根据族谱id,新增户主
// export async function createHouse(parms) {
// 	const res = await request({
// 					url:"/functionMenu/collectiontask/useradd",
// 					method:"post",
// 					data:parms
// 				})
	
// 	return res
// }

export async function createHouse(parms) {
	const res = await request({
					url:"/functionMenu/householder/add",
					method:"post",
					data:parms
				})
	
	return res
}
export async function getHouse(id) {
	const res = await request({
					url:"/functionMenu/householder/list",
					method:"get",
					data:{
						createid:id
					}
				})
	
	return res
}
      //新增户主家庭成员

export async function addFamily(parms) {
	const res = await request({
					url:"/functionMenu/relatives/add",
					method:"post",
					data:parms
				})
	
	return res
}

     //删除家庭成员  /functionMenu/relatives/{ids}
export async function delFamily(id) {
	const res = await request({
					url:"/functionMenu/relatives/" + id,
					method:"DELETE"
				})
	
	return res
}