package com.opzpy123.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Slf4j
public class TreeUtil {

    /**
     * 根据树状节点的List列表，构造出树状结构信息，原先对象列表中的根节点做为返回值，其余节点按父子关系放置在根节点的childListName属性中，以List形式记录,
     * 并以sequenceName从小到大排序（如果sequenceName存在）
     *
     * @param treeNodeList  树状节点列表
     * @param idName        节点标识的属性名
     * @param parentIdName  父节点标识的属性名
     * @param childListName 子节点集合的属性名，该属性必须是List类型的
     * @param sequenceName  如果返回的子节点集合需要排序，必须知道排序的属性名，该参数为null则不排序，参与排序的属性名必须是可排序的元类型，例如Long, Integer, String等
     * @param leafFlagName  如果需要设置节点的叶节点标记，则指定该属性名， 该属性必须是布尔类型的
     * @return
     */
    public static <T> T buildTree(List<T> treeNodeList, String idName, String parentIdName, String childListName, String sequenceName, String leafFlagName) {
        //按照每个节点的ID信息，构造HashMap，便于后面的快速定位，顺便定位出根节点
        Map<Object, T> treeNodeMap = new HashMap<>();
        T rootNode = null;

        for (T node : treeNodeList) {
            Object key = getPropertyValue(node, idName);
            treeNodeMap.put(key, node);

            if (getPropertyValue(node, parentIdName) == null) {
                rootNode = node;
            }
            if (StringUtils.isNotBlank(leafFlagName)) {
                //先将每个节点都设置为叶节点，后面如果该节点增加了Child信息，则设置为False
                setPropertyValue(node, leafFlagName, Boolean.TRUE);
            }
        }

        //有一种情况是，根节点有可能是某个更大树结构下面的子节点，这时所有的parentId都是有值的，但是在当前列表中定位不到，那么这种节点也可以作为根节点
        for (T node : treeNodeList) {
            Object parentId = getPropertyValue(node, parentIdName);
            //此处不能用if(parentId)来判断,因为id有可能为0或者负数这里会判断为false，识认为其没有id
            if (parentId != null) {
                T parentNode = treeNodeMap.get(parentId);
                if (parentNode != null) {
                    if (getPropertyValue(parentNode, childListName) == null) {
                        List<T> childList = new ArrayList<>();
                        childList.add(node);

                        setPropertyValue(parentNode, childListName, childList);
                    } else {
                        List<T> childList = (List) getPropertyValue(parentNode, childListName);
                        childList.add(node);
                    }

                    if (StringUtils.isNotBlank(leafFlagName)) {
                        setPropertyValue(parentNode, leafFlagName, Boolean.FALSE);
                    }
                } else {
                    rootNode = node;
                }
            }
        }

        //根据是否需要排序进行处理
        if (StringUtils.isNotBlank(sequenceName)) {
            for (T node : treeNodeList) {
                Object childObject = getPropertyValue(node, childListName);
                if (childObject != null) {
                    List<T> childList = (List) childObject;
                    childList.sort((o1, o2) -> comparableProperty(o1, o2, sequenceName));
                }
            }
        }

        return rootNode;
    }

    /**
     * 根据树状节点的List列表，构造出树状结构信息，原先对象列表中的无父节点的做为根节点返回，支持有多个根节点，
     * 其余节点按父子关系放置在根节点的childListName属性中，以List形式记录, 并以sequenceName从小到大排序（如果sequenceName存在）
     *
     * @param treeNodeList  树状节点列表
     * @param idName        节点标识的属性名
     * @param parentIdName  父节点标识的属性名
     * @param childListName 子节点集合的属性名，该属性必须是List类型的
     * @param sequenceName  如果返回的子节点集合需要排序，必须知道排序的属性名，该参数为null则不排序，参与排序的属性名必须是可排序的元类型，例如Long, Integer, String等
     * @param leafFlagName  如果需要设置节点的叶节点标记，则指定该属性名， 该属性必须是布尔类型的
     * @return
     */
    public static <T> List<T> buildTrees(List<T> treeNodeList, String idName, String parentIdName, String childListName, String sequenceName, String leafFlagName) {
        //按照每个节点的ID信息，构造HashMap，便于后面的快速定位，顺便定位出根节点
        Map<Object, T> treeNodeMap = new HashMap<>();
        Map<Object, T> rootNodeMap = new HashMap<>();

        for (T node : treeNodeList) {
            Object key = getPropertyValue(node, idName);
            treeNodeMap.put(key, node);

            if (getPropertyValue(node, parentIdName) == null) {
                rootNodeMap.put(key, node);
            }
            if (StringUtils.isNotBlank(leafFlagName)) {
                //先将每个节点都设置为叶节点，后面如果该节点增加了Child信息，则设置为False
                setPropertyValue(node, leafFlagName, Boolean.TRUE);
            }
        }

        //有一种情况是，根节点有可能是某个更大树结构下面的子节点，这时所有的parentId都是有值的，但是在当前列表中定位不到，那么这种节点也可以作为根节点
        for (T node : treeNodeList) {
            Object parentId = getPropertyValue(node, parentIdName);
            if (parentId != null) {
                T parentNode = treeNodeMap.get(parentId);
                if (parentNode != null) {
                    if (getPropertyValue(parentNode, childListName) == null) {
                        List<T> childList = new ArrayList<>();
                        childList.add(node);

                        setPropertyValue(parentNode, childListName, childList);
                    } else {
                        List<T> childList = (List) getPropertyValue(parentNode, childListName);
                        childList.add(node);
                    }

                    if (StringUtils.isNotBlank(leafFlagName)) {
                        setPropertyValue(parentNode, leafFlagName, Boolean.FALSE);
                    }
                } else {
                    rootNodeMap.put(getPropertyValue(node, idName), node);
                }
            }
        }

        //根据是否需要排序进行处理
        if (StringUtils.isNotBlank(sequenceName)) {
            for (T node : treeNodeList) {
                Object childObject = getPropertyValue(node, childListName);
                if (childObject != null) {
                    List<T> childList = (List) childObject;
                    childList.sort((o1, o2) -> comparableProperty(o1, o2, sequenceName));
                }
            }
        }

        //将根节点的Map变成数组，并也考虑多个根节点的排序
        List<T> rootList = new ArrayList<>(rootNodeMap.values());
        if (StringUtils.isNotBlank(sequenceName)) {
            rootList.sort((o1, o2) -> comparableProperty(o1, o2, sequenceName));
        }

        return rootList;
    }

    private static Object getPropertyValue(Object obj, String propertyName) {
        Object value = null;
        try {
            value = PropertyUtils.getProperty(obj, propertyName);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return value;
    }

    /**
     * 把树形结构的数据按照前序遍历的顺序放在list中并返回
     *
     * @param root          树的根
     * @param idName        表示id的属性名
     * @param parentIdName  表示parentId的属性名
     * @param childListName 子节点集合的属性名
     * @param comparator    比较器，根据此比较器在同一层级排序，从大到小
     * @param generatId     是否生成临时Id,如果idName代表的属性和原数据id相同，则原id被设置为新生成的Id,如果此数据是hibernate的持久化数据，可能会抛出Exception
     * @param rootId        生成临时id的种子id，当前使用递增的方法来生成id
     * @param levelName     层级的属性名
     * @param rootLevel     根层级
     * @return
     */
    public static List listTree(Object root, String idName, String parentIdName, String childListName, Comparator comparator, boolean generatId, Long rootId,
                                String levelName, Integer rootLevel) {
        List collection = new ArrayList();
        //用来保存当前id
        Map<String, Long> idGenerator = new HashMap(1);
        idGenerator.put("currentId", rootId);
        addNode(root, collection, idName, parentIdName, childListName, comparator, generatId, idGenerator, levelName, rootLevel);
        return collection;
    }

    private static void addNode(Object node, List collection, String idName, String parentIdName, String childListName, Comparator comparator, boolean generatId,
                                Map<String, Long> idGenerator, String levelName, Integer rootLevel) {
        //处理层级
        if (StringUtils.isNotBlank(levelName)) {
            if (rootLevel == null) rootLevel = 0;
            setPropertyValue(node, levelName, rootLevel++);
        }

        //如果设置自动生成id,则把node的id设置成生成的id值，当前id+1
        if (generatId) {
            Long id = idGenerator.get("currentId");
            idGenerator.put("currentId", id + 1);
            setPropertyValue(node, idName, id);
        }

        collection.add(node);
        List children = (List) getPropertyValue(node, childListName);
        if (children != null) {
            if (comparator != null) {
                Collections.sort(children, comparator);
            }

            for (Object c : children) {
                if (StringUtils.isNotBlank(parentIdName)) {
                    setPropertyValue(c, parentIdName, getPropertyValue(node, idName));
                }
                addNode(c, collection, idName, parentIdName, childListName, comparator, generatId, idGenerator, levelName, rootLevel);
            }
        }
    }

    private static void setPropertyValue(Object obj, String propertyName, Object propertyValue) {
        try {
            PropertyUtils.setProperty(obj, propertyName, propertyValue);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private static int comparableProperty(Object obj1, Object obj2, String propertyName) {
        int comparableResult = 0;
        try {
            Object sequenceValue1 = PropertyUtils.getProperty(obj1, propertyName);
            Object sequenceValue2 = PropertyUtils.getProperty(obj2, propertyName);
            if (sequenceValue1 instanceof Comparable) {
                comparableResult = ((Comparable) sequenceValue1).compareTo(sequenceValue2);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return comparableResult;
    }
}
