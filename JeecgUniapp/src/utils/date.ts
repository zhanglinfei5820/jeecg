/**
 * 格式化日期
 * @param date 日期对象或日期字符串
 * @param format 格式化模板，默认 'YYYY-MM-DD HH:mm:ss'
 */
export const formatDate = (date: Date | string, format = 'YYYY-MM-DD HH:mm:ss') => {
  if (!date) return ''
  
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  
  return format
    .replace('YYYY', String(year))
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}

/**
 * 计算两个日期之间的天数差
 * @param date1 日期1
 * @param date2 日期2
 */
export const getDaysDiff = (date1: Date | string, date2: Date | string) => {
  const d1 = new Date(date1)
  const d2 = new Date(date2)
  return Math.floor((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24))
} 